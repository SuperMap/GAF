/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.data.AltitudeMode;
import com.supermap.gaf.common.storage.client.StorageClient;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.webgis.dao.WebgisRoamRouteMapper;
import com.supermap.gaf.webgis.domain.WebgisRouteInfo;
import com.supermap.gaf.webgis.domain.WebgisRouteStopInfo;
import com.supermap.gaf.webgis.entity.WebgisRoamRoute;
import com.supermap.gaf.webgis.entity.WebgisRoamStop;
import com.supermap.gaf.webgis.service.WebgisRoamRouteService;
import com.supermap.gaf.webgis.service.WebgisRoamStopService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisRoamRouteSelectVo;
import com.supermap.realspace.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.security.sasl.AuthenticationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 漫游路线服务实现类
 *
 * @author wangxiaolong
 * @date yyyy-mm-dd
 */
@Service
public class WebgisRoamRouteServiceImpl implements WebgisRoamRouteService {

    private static final Logger log = LoggerFactory.getLogger(WebgisRoamRouteServiceImpl.class);
    private static final String FPF_TEMPLATE_PATH = "template/fpf/default_template.fpf";


    /**
     * 表示系统临时文件夹
     */
    public static String SYSTEM_TEMP_DIRECTORY="java.io.tmpdir";


    private final WebgisRoamRouteMapper webgisRoamRouteMapper;

    private final WebgisRoamStopService webgisRoamStopService;

    @Value("${gaf.map.fly-manager.file-path:/public/map/fpf}")
    String routeFilePath;

    @Autowired
    @Qualifier("WebgisStorageClient")
    private StorageClient storageClient;

    public WebgisRoamRouteServiceImpl(WebgisRoamRouteMapper webgisRoamRouteMapper, WebgisRoamStopService webgisRoamStopService) {
        this.webgisRoamRouteMapper = webgisRoamRouteMapper;
        this.webgisRoamStopService = webgisRoamStopService;
    }


    /**
     * 加载模板文件，获取FlyManager对象
     *
     * @return
     */
    private FlyManager getFlyManagerFromTemplate() throws FileNotFoundException {
        File f = new File("default_template.fpf");
        if (!f.exists()) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = loader.getResourceAsStream(FPF_TEMPLATE_PATH);
                 FileOutputStream outputStream = new FileOutputStream(f)) {
                if (inputStream == null) {
                    log.error("飞行路线模板：default_template.fpf");
                }
                IOUtils.copy(inputStream, outputStream);
            } catch (Exception ex) {
                log.error("飞行路线模板文件复制出错", ex);
            }
        }
        if (!f.exists()) {
            throw new FileNotFoundException("飞行路线模板文件default_template.fpf不存在");
        }
        log.debug("脚本位置:{}", f.toString());
        log.info("getFlyManagerFromTemplate-path:" + f.toPath().toString());
        return getFlyManager(f.toPath());
    }

    private FlyManager getFlyManager(Path path) {
        boolean exists = path.toFile().exists();
        if (!exists) {
            throw new GafException("模板fpf文件不存在");
        }
        FlyManager flyManager = new FlyManager();
        boolean loadSuccess = flyManager.getRoutes().fromFile(path.toString());
        if (!loadSuccess) {
            throw new GafException("加载模板fpf文件失败");
        }
        if (flyManager.getRoutes().getCount() != 1) {
            throw new GafException("飞行路径模板文件错误,应该只存在一条飞行路径");
        }
        return flyManager;
    }


    @Override
    public WebgisRoamRoute getById(String gisRoamRouteId) {
        if (gisRoamRouteId == null) {
            throw new IllegalArgumentException("gisRoamRouteId不能为空");
        }
        return webgisRoamRouteMapper.select(gisRoamRouteId);
    }

    @Override
    public List<WebgisRouteStopInfo> listRouteStops(String routeId) {
        WebgisRoamRoute webgisRoamRoute = getById(routeId);
        if (webgisRoamRoute == null) {
            return null;
        }
        List<WebgisRoamStop> roamStops = webgisRoamStopService.listStops(webgisRoamRoute.getGisRoamRouteId());
        return roamStops.stream().sorted(Comparator.comparingInt(WebgisRoamStop::getSortSn)).map(roamStop -> {
            WebgisRouteStopInfo stopInfo = new WebgisRouteStopInfo();
            stopInfo.setStopId(roamStop.getGisRoamStopId());
            stopInfo.setAltitude(roamStop.getAltitude());
            stopInfo.setAltitudeMode(roamStop.getAltitudemode());
            stopInfo.setHeading(roamStop.getHeading());
            stopInfo.setLatitude(roamStop.getLatitude());
            stopInfo.setLongitude(roamStop.getLongitude());
            stopInfo.setSpeed(roamStop.getSpeed());
            stopInfo.setName(roamStop.getName());
            stopInfo.setTilt(roamStop.getTilt());
            stopInfo.setUseMySpeed(roamStop.getUsemyspeed());
            stopInfo.setData("{ \"x\": " + roamStop.getSightCenterX() + ",\"y\": " + roamStop.getSightCenterY() + ",\"z\": " + roamStop.getSightCenterZ() + "}");
            return stopInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<WebgisRoamRoute> listByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new GafException("路线名不能为空");
        }
        ShiroUser user = SecurityUtilsExt.getUser();
        WebgisRoamRouteSelectVo selectVo = new WebgisRoamRouteSelectVo();
        selectVo.setUserId(Objects.requireNonNull(user).getAuthUser().getUserId());
        selectVo.setName(name);
        return webgisRoamRouteMapper.selectList(selectVo);
    }


    @Override
    public Page<WebgisRoamRoute> listByPageCondition(WebgisRoamRouteSelectVo webgisRoamRouteSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisRoamRoute> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> webgisRoamRouteMapper.selectList(webgisRoamRouteSelectVo));
        return Page.create(pageInfo.getPageNum(), pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    private Path getFpfFilePath(String userId, String fileName) {
        return Paths.get(routeFilePath, userId, fileName + ".fpf");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebgisRouteInfo createRoute(WebgisRouteInfo webgisRouteInfo) throws IOException {
        // 校验当前用户下是否存在路径名字
        List<WebgisRoamRoute> webgisRoamRoutes = listByName(webgisRouteInfo.getName());
        if (webgisRoamRoutes.size() > 0) {
            throw new GafException("路线名已存在");
        }
        FlyManager flyManager = getFlyManagerFromTemplate();
        fillRoute(webgisRouteInfo, flyManager.getRoutes().get(0));
        ShiroUser user = SecurityUtilsExt.getUser();
        Path fpfFilePath = getFpfFilePath(user.getAuthUser().getUserId(), webgisRouteInfo.getName());
        generateFpfFile(flyManager, fpfFilePath);
        WebgisRoamRoute roamRoute = new WebgisRoamRoute();
        roamRoute.setGisRoamRouteId(UUID.randomUUID().toString());
        roamRoute.setUserId(user.getAuthUser().getUserId());
        roamRoute.setName(webgisRouteInfo.getName());
        roamRoute.setSpeed(webgisRouteInfo.getSpeed());
        roamRoute.setCreatedBy(user.getAuthUser().getName());
        roamRoute.setUpdatedBy(user.getAuthUser().getName());
        roamRoute.setFpfPath(fpfFilePath.toString());
        webgisRoamRouteMapper.insert(roamRoute);
        webgisRouteInfo.setRouteId(roamRoute.getGisRoamRouteId());
        return webgisRouteInfo;
    }



    /**
     * 生成fpf文件
     *
     * @param flyManager 飞行管理对象
     * @param path       路径
     */
    private synchronized void generateFpfFile(FlyManager flyManager, Path path) throws IOException {
        String systemTempDir = System.getProperties().getProperty(SYSTEM_TEMP_DIRECTORY);
        Path tmpDir = Paths.get(systemTempDir, UUID.randomUUID().toString());
        if (!Files.exists(tmpDir)) {
            Files.createDirectories(tmpDir);
        }
        Path tmp = Paths.get(tmpDir.toString(),path.getFileName().toString());
        flyManager.getRoutes().toFile(tmp.toString());
        String tenantId = SecurityUtilsExt.getUser().getAuthUser().getTenantId();
        storageClient.delete(removeStartedBackslash(path.toString()), tenantId);
        storageClient.uploadFlie(removeStartedBackslash(path.toString()), tenantId,tmp.toFile());
        Files.deleteIfExists(tmp);
    }

    /**
     * 用webgisRouteInfo填充route
     *
     * @param webgisRouteInfo 单条路线信息
     * @param route           see com.supermap.realspace.Route
     */
    private void fillRoute(WebgisRouteInfo webgisRouteInfo, Route route) {
        if (webgisRouteInfo.getSpeed() != -1) {
            route.setSpeed(webgisRouteInfo.getSpeed());
        }
        route.setName(webgisRouteInfo.getName());
        route.setFlyingLoop(webgisRouteInfo.isFlyingLoop());
        route.setFlyAlongTheRoute(webgisRouteInfo.isFlyAlongTheRoute());
        route.setAltitudeFixed(webgisRouteInfo.isAltitudeFixed());
        route.setHeadingFixed(webgisRouteInfo.isHeadingFixed());
        route.setTiltFixed(webgisRouteInfo.isTiltFixed());
        if (webgisRouteInfo.isAltitudeModeIsAbsolute()) {
            route.getDefaultStyle().setAltitudeMode(AltitudeMode.ABSOLUTE);
        } else {
            route.getDefaultStyle().setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);
        }
    }


    /**
     * 用webgisRoamStop填充routeStop
     */
    private void fillRouteStop(WebgisRoamStop webgisRoamStop, RouteStop routeStop) {
        routeStop.setName(webgisRoamStop.getName());
        routeStop.setSpeed(webgisRoamStop.getSpeed());
        Camera camera = new Camera();
        camera.setHeading(webgisRoamStop.getHeading());
        camera.setAltitude(webgisRoamStop.getAltitude());
        camera.setLatitude(webgisRoamStop.getLatitude());
        camera.setLongitude(webgisRoamStop.getLongitude());
        camera.setTilt(webgisRoamStop.getTilt());
        if (StringUtils.isEmpty(webgisRoamStop.getAltitudemode())) {
            camera.setAltitudeMode(AltitudeMode.ABSOLUTE);
        } else {
            camera.setAltitudeMode((AltitudeMode) AltitudeMode.parse(AltitudeMode.class, webgisRoamStop.getAltitudemode()));
        }
        routeStop.setCamera(camera);
    }


    @Override
    public void batchInsert(List<WebgisRoamRoute> webgisRoamRoutes) {
        if (webgisRoamRoutes != null && webgisRoamRoutes.size() > 0) {
            webgisRoamRoutes.forEach(webgisRoamRoute -> {
                webgisRoamRoute.setGisRoamRouteId(UUID.randomUUID().toString());
                ShiroUser shiroUser = SecurityUtilsExt.getUser();
                webgisRoamRoute.setCreatedBy(shiroUser.getAuthUser().getName());
                webgisRoamRoute.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisRoamRouteMapper.batchInsert(webgisRoamRoutes);
        }

    }


    @Override
    public void batchDelete(List<String> gisRoamRouteIds) {
        webgisRoamRouteMapper.batchDelete(gisRoamRouteIds);
    }

    @Override
    public WebgisRoamRoute updateWebgisRoamRoute(WebgisRoamRoute webgisRoamRoute) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisRoamRoute.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisRoamRouteMapper.update(webgisRoamRoute);
        return webgisRoamRoute;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRoute(String routeId) throws IOException {
        WebgisRoamRoute webgisRoamRoute = getById(routeId);
        if (webgisRoamRoute == null) {
            throw new GafException("路线不存在");
        }
        storageClient.delete(removeStartedBackslash(webgisRoamRoute.getFpfPath()),SecurityUtilsExt.getUser().getAuthUser().getTenantId());
        webgisRoamRouteMapper.delete(webgisRoamRoute.getGisRoamRouteId());
        webgisRoamStopService.removeByRouteId(webgisRoamRoute.getGisRoamRouteId());
    }


    @Transactional
    @Override
    public void clearRouteStops(String routeId) throws IOException {
        WebgisRoamRoute webgisRoamRoute = getById(routeId);
        if (webgisRoamRoute == null) {
            return;
        }
        webgisRoamStopService.removeByRouteId(webgisRoamRoute.getGisRoamRouteId());
        generateFpfFile(webgisRoamRoute);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRouteStops(WebgisRouteInfo webgisRouteInfo) throws IOException {
        // 数据库查询有没有线路和重复的站点名
        List<WebgisRoamRoute> webgisRoamRoutes = listByName(webgisRouteInfo.getName());
        if (webgisRoamRoutes.isEmpty()) {
            throw new GafException("路线不存在");
        }
        List<WebgisRoamStop> webgisRoamStops = webgisRoamStopService.listStops(webgisRoamRoutes.get(0).getGisRoamRouteId());
        Set<String> stopNames = webgisRoamStops.stream().map(WebgisRoamStop::getName).collect(Collectors.toSet());
        WebgisRouteStopInfo[] needAddStops = webgisRouteInfo.getStops();
        if (needAddStops != null && needAddStops.length > 0) {
            for (WebgisRouteStopInfo needAddStop : needAddStops) {
                if (stopNames.contains(needAddStop.getName())) {
                    throw new GafException("站点名" + needAddStop.getName() + "已存在");
                }
            }
            int exitStopsCount = webgisRoamStops.size();
            List<WebgisRoamStop> roamStops = new LinkedList<>();
            for (int i = 0; i < needAddStops.length; i++) {
                WebgisRouteStopInfo stopInfo = needAddStops[i];
                WebgisRoamStop webgisRoamStop = new WebgisRoamStop();
                webgisRoamStop.setGisRoamRouteId(webgisRoamRoutes.get(0).getGisRoamRouteId());
                webgisRoamStop.setAltitude(stopInfo.getAltitude());
                webgisRoamStop.setAltitudemode(stopInfo.getAltitudeMode());
                webgisRoamStop.setAltitude(stopInfo.getAltitude());
                webgisRoamStop.setGisRoamStopId(UUID.randomUUID().toString());
                webgisRoamStop.setHeading(stopInfo.getHeading());
                webgisRoamStop.setLatitude(stopInfo.getLatitude());
                webgisRoamStop.setLongitude(stopInfo.getLongitude());
                webgisRoamStop.setName(StringUtils.isEmpty(stopInfo.getName()) ? "stop_" + exitStopsCount + i + 1 : stopInfo.getName());
                webgisRoamStop.setSpeed(stopInfo.getSpeed());
                webgisRoamStop.setStatus(true);
                webgisRoamStop.setTilt(stopInfo.getTilt());
                webgisRoamStop.setUsemyspeed(stopInfo.isUseMySpeed());
                String data = stopInfo.getData();
                JSONObject jsonObject = JSONObject.parseObject(data);
                Double x = jsonObject.getDouble("x");
                Double y = jsonObject.getDouble("y");
                Double z = jsonObject.getDouble("z");
                webgisRoamStop.setSightCenterX(x);
                webgisRoamStop.setSightCenterY(y);
                webgisRoamStop.setSightCenterZ(z);
                webgisRoamStop.setSortSn(exitStopsCount + i + 1);
                roamStops.add(webgisRoamStop);
            }
            if (!roamStops.isEmpty()) {
                webgisRoamStopService.batchInsert(roamStops);
            }
            // 新增后生成新的路线文件
            generateFpfFile(webgisRoamRoutes.get(0));
        }
    }

    @Override
    public List<WebgisRouteInfo> listRoutes() {
        ShiroUser user = SecurityUtilsExt.getUser();
        WebgisRoamRouteSelectVo selectVo = new WebgisRoamRouteSelectVo();
        selectVo.setUserId(user.getAuthUser().getUserId());
        List<WebgisRoamRoute> roamRoutes = webgisRoamRouteMapper.selectList(selectVo);
        return roamRoutes.stream().map(roamRoute -> {
            WebgisRouteInfo routeInfo = new WebgisRouteInfo();
            routeInfo.setRouteId(roamRoute.getGisRoamRouteId());
            routeInfo.setName(roamRoute.getName());
            routeInfo.setFilePath(roamRoute.getFpfPath());
            return routeInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public synchronized String getRouteFileUrl(String routeId) throws URISyntaxException, AuthenticationException {
        WebgisRoamRoute roamRoute = getById(routeId);
        if (roamRoute == null || StringUtils.isEmpty(roamRoute.getFpfPath())) {
            return null;
        }
        String fpfPath = roamRoute.getFpfPath();
        return storageClient.getVolumePath(removeStartedBackslash(fpfPath), SecurityUtilsExt.getUser().getAuthUser().getTenantId(), true).getPublicUrl();
    }

    // 移除路径开头的反斜杠
    private String removeStartedBackslash(String path) {
        if (path.startsWith("/")) {
            return path.replaceFirst("/","");
        }
        return path;
    }

    private WebgisRoamRoute generateFpfFile(WebgisRoamRoute roamRoute) throws IOException {
        if (roamRoute == null) {
            return null;
        }
        FlyManager flyManager = getFlyManagerFromTemplate();
        Route route = flyManager.getRoutes().get(0);
        route.setName(roamRoute.getName());
        route.setSpeed(roamRoute.getSpeed());
        RouteStops stops = route.getStops();
        stops.clear();
        List<WebgisRoamStop> roamStops = webgisRoamStopService.listStops(roamRoute.getGisRoamRouteId());
        for (WebgisRoamStop roamStop : roamStops) {
            RouteStop routeStop = new RouteStop();
            fillRouteStop(roamStop, routeStop);
            stops.add(routeStop);
        }
        generateFpfFile(flyManager, Paths.get(roamRoute.getFpfPath()));
        return roamRoute;
    }

}
