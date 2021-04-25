package com.supermap.gaf.data.mgt.service.publisher.core.registry;

/**
* @author:yw
* @Date 2021-3-12
 * 服务发布者类型
 */
public enum PublisherType {
    /**
    * workspace
    **/
    WORKSPACE("workspace"),
    /**
     * hbase
     **/
    HBASE("hbase");

    private String type;

    PublisherType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
