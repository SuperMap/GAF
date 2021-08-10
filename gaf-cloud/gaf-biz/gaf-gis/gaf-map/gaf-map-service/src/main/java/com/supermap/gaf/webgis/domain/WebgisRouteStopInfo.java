/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.domain;

import io.swagger.annotations.ApiModelProperty;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class WebgisRouteStopInfo {
    @ApiModelProperty("站点id")
    private String stopId;
    @ApiModelProperty("相机的方位角（上方向，即和北方的夹角），方位角的范围是0-360度。")
    private double heading;
    @ApiModelProperty("纬度")
    private double latitude;
    @ApiModelProperty("经度")
    private double longitude;
    @ApiModelProperty("高度")
    private double altitude;
    @ApiModelProperty("仰角")
    private double tilt;
    @ApiModelProperty("使用当前站点速度")
    private boolean useMySpeed;
    @ApiModelProperty("当前速度 米/秒")
    private double speed;
    private String name;
    @ApiModelProperty("飞入方式")
    private String altitudeMode;

    private String data;

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }


    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getTilt() {
        return tilt;
    }

    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public boolean isUseMySpeed() {
        return useMySpeed;
    }

    public void setUseMySpeed(boolean useMySpeed) {
        this.useMySpeed = useMySpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(String altitudeMode) {
        this.altitudeMode = altitudeMode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
