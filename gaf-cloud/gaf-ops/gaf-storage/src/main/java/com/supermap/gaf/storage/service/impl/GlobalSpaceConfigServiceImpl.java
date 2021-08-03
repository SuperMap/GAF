package com.supermap.gaf.storage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.storage.dao.S3ServerMapper;
import com.supermap.gaf.storage.dao.SpaceMapper;
import com.supermap.gaf.storage.entity.S3Server;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.SpaceConfig;
import com.supermap.gaf.storage.entity.vo.SpaceConfigSelectVo;
import com.supermap.gaf.storage.entity.vo.SpaceSelectVo;
import com.supermap.gaf.storage.enums.CreatedType;
import com.supermap.gaf.storage.enums.TargetType;
import com.supermap.gaf.storage.service.GlobalSpaceConfigService;
import com.supermap.gaf.storage.utils.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 服务实现类
 *
 * @author zrc
 * @date yyyy-mm-dd
 */
@Service
public class GlobalSpaceConfigServiceImpl implements GlobalSpaceConfigService {

    private static final Logger log = LoggerFactory.getLogger(GlobalSpaceConfigServiceImpl.class);

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private S3ServerMapper s3ServerMapper;


    @Override
    public SpaceConfig getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        List<SpaceConfig> spaceConfigs = spaceMapper.selectSpaceConfig(SpaceConfigSelectVo.builder().id(id).targetType(TargetType.PLATFORM.getValue()).build());
        if (CollectionUtils.isEmpty(spaceConfigs)) {
            return null;
        } else {
            return spaceConfigs.get(0);
        }
    }

    @Override
    public Page<SpaceConfig> listByPageCondition(SpaceConfigSelectVo spaceConfigSelectVo, int pageNum, int pageSize) {
        spaceConfigSelectVo.setTargetType(TargetType.PLATFORM.getValue());
        spaceMapper.selectSpaceConfig(spaceConfigSelectVo);
        PageInfo<SpaceConfig> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            spaceMapper.selectSpaceConfig(spaceConfigSelectVo);
        });
        return Page.create(pageInfo.getPageNum(), pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }


    @Override
    @Transactional
    public void insertGlobalServerConfig(SpaceConfig spaceConfig) {
        String s3ServerId = UUID.randomUUID().toString();
        S3Server s3Server = S3Server.builder().id(s3ServerId).accessKey(spaceConfig.getAccessKey()).serviceEndpoint(spaceConfig.getServiceEndpoint())
                .secretKey(spaceConfig.getSecretKey()).build();
        s3ServerMapper.insert(s3Server);
        Space space = Space.builder().id(UUID.randomUUID().toString()).name(spaceConfig.getName()).createdType(CreatedType.CREATED.getValue()).target("").targetType(TargetType.PLATFORM.getValue())
                .parentSpaceId(s3ServerId).storageName(spaceConfig.getBucketName()).description(spaceConfig.getDescription())
                .totalSize(spaceConfig.getTotalSize()).build();
        spaceMapper.insert(space);
    }


    @Override
    public void deleteGlobalServerConfig(String id) {
        List<Space> spaces = spaceMapper.selectList(SpaceSelectVo.builder().id(id).targetType(TargetType.PLATFORM.getValue()).build());
        if (!CollectionUtils.isEmpty(spaces)) {
            Space space = spaces.get(0);
            s3ServerMapper.delete(space.getParentSpaceId());
            spaceMapper.delete(id);
        }
    }

    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        List<Space> spaces = spaceMapper.selectByIdsAndTargetTypeAndTargetId(ids, TargetType.PLATFORM.getValue(), "");
        if (!CollectionUtils.isEmpty(spaces)) {
            List<String> s3ServerIds = new ArrayList<>();
            List<String> spaceIds = new ArrayList<>();
            for (Space item : spaces) {
                s3ServerIds.add(item.getParentSpaceId());
                spaceIds.add(item.getId());
            }
            s3ServerMapper.batchDelete(s3ServerIds);
            spaceMapper.batchDelete(spaceIds);
        }
    }


    @Override
    @Transactional
    public void updateGlobalServerConfig(SpaceConfig spaceConfig) {
        List<Space> spaces = spaceMapper.selectList(SpaceSelectVo.builder().id(spaceConfig.getId()).targetType(TargetType.PLATFORM.getValue()).build());
        if (!CollectionUtils.isEmpty(spaces)) {
            Space space = spaces.get(0);
            S3Server s3Server = S3Server.builder().id(space.getParentSpaceId()).accessKey(spaceConfig.getAccessKey()).serviceEndpoint(spaceConfig.getServiceEndpoint())
                    .secretKey(spaceConfig.getSecretKey()).build();
            s3ServerMapper.update(s3Server);
            Space spaceUpdate = Space.builder().createdType(CreatedType.CREATED.getValue()).target("").targetType(TargetType.PLATFORM.getValue()).build();
            BeanUtils.copyProperties(spaceConfig, spaceUpdate);
            spaceMapper.update(spaceUpdate);

        }
    }

}
