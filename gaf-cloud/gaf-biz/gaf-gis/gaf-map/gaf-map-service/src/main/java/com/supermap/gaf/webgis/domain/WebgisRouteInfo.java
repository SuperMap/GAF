/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author:yj
 * @date:2021/3/25
 * 飞行路线信息
 */
public class WebgisRouteInfo {
    @ApiModelProperty("路线id")
    private String routeId;
    @ApiModelProperty("路线名称")
    private String name;
    @ApiModelProperty("站点列表")
    private WebgisRouteStopInfo[] stops;
    @ApiModelProperty("是否锁定高层")
    boolean altitudeFixed;
    @ApiModelProperty("是否沿线飞行")
    boolean flyAlongTheRoute;
    @ApiModelProperty("是否循环飞行")
    boolean flyingLoop;
    @ApiModelProperty("是否锁定方位角")
    boolean headingFixed;
    @ApiModelProperty("是否锁定仰角")
    boolean tiltFixed;
    @ApiModelProperty("true:高度模式为绝对高度, false:为贴地")
    boolean altitudeModeIsAbsolute;
    @ApiModelProperty("飞行速度")
    double speed;
    @ApiModelProperty("修改或删除站点的索引值")
    int indexOfEditStop;
    @ApiModelProperty("路线所在的文件路径")
    String filePath;

    public WebgisRouteInfo()
    {
        indexOfEditStop = -1;
        this.speed = -1;
        tiltFixed = true;
        altitudeFixed = true;
        headingFixed= true;

    }
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WebgisRouteStopInfo[] getStops() {
        return stops;
    }

    public void setStops(WebgisRouteStopInfo[] stops) {
        this.stops = stops;
    }

    public boolean isAltitudeFixed() {
        return altitudeFixed;
    }

    public void setAltitudeFixed(boolean altitudeFixed) {
        this.altitudeFixed = altitudeFixed;
    }

    public boolean isFlyAlongTheRoute() {
        return flyAlongTheRoute;
    }

    public void setFlyAlongTheRoute(boolean flyAlongTheRoute) {
        this.flyAlongTheRoute = flyAlongTheRoute;
    }

    public boolean isFlyingLoop() {
        return flyingLoop;
    }

    public void setFlyingLoop(boolean flyingLoop) {
        this.flyingLoop = flyingLoop;
    }

    public boolean isHeadingFixed() {
        return headingFixed;
    }

    public void setHeadingFixed(boolean headingFixed) {
        this.headingFixed = headingFixed;
    }

    public boolean isTiltFixed() {
        return tiltFixed;
    }

    public void setTiltFixed(boolean tiltFixed) {
        this.tiltFixed = tiltFixed;
    }

    public boolean isAltitudeModeIsAbsolute() {
        return altitudeModeIsAbsolute;
    }

    public void setAltitudeModeIsAbsolute(boolean altitudeModeIsAbsolute) {
        this.altitudeModeIsAbsolute = altitudeModeIsAbsolute;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getIndexOfEditStop() {
        return indexOfEditStop;
    }

    public void setIndexOfEditStop(int indexOfEditStop) {
        this.indexOfEditStop = indexOfEditStop;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean hasStops(){
        return this.stops != null && this.stops.length > 0;
    }

}
