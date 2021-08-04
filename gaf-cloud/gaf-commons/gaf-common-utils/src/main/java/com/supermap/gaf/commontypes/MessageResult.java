/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <p>
 * 统一返回结果类型
 * </p>
 *
 * @author t450
 * @version ${Version}
 * @date 2018年5月15日
 * @since 1.0.0
 */
@ApiModel("REST返回数据对象")
public class MessageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String SUCCEED = "succeed";

    /**
     * 是否成功
     */
    @JsonProperty("isSuccessed")
    @ApiModelProperty("请求成功状态")
    private boolean isSuccessed = false;
    /**
     * 返回结果的信息记录 备注：记录错误信息保持简洁 例如 XXXX执行成功，XXXX执行错误，XXXX访问数据库异常
     */
    @ApiModelProperty("返回结果的信息记录")
    private String message;
    /**
     * 返回结果状态吗
     */
    @ApiModelProperty("返回结果状态码")
    private int status;
    /**
     * 返回参数实体记录
     */
    @ApiModelProperty("返回参数实体记录")
    private T data;
    /**
     * 返回资源的id
     */
    @ApiModelProperty("返回资源的id")
    private String resourceId;

    /**
     * <p>
     * 获取 是否成功
     * </p>
     *
     * @return 属性 isSuccessed 的值。
     * @since 1.0.0
     */
    @JSONField(name = "isSuccessed")
    @JsonIgnore
    public boolean IsSuccessed() {
        return isSuccessed;
    }

    /**
     * <p>
     * 设置 是否成功
     * </p>
     *
     * @param isSuccessed the isSuccessed to set
     * @since 1.0.0
     */
    @JSONField(name = "isSuccessed")
    public void setSuccessed(boolean isSuccessed) {
        this.isSuccessed = isSuccessed;
    }

    /**
     * @return 属性 message 的值。
     * @since 1.0.0
     */
    public String getMessage() {
        if (StringUtils.isEmpty(message) && this.isSuccessed) {
            message = "操作成功";
        }
        return message;
    }

    /**
     * <p>
     * 设置信息 备注：记录错误信息保持简洁 例如 XXXX执行成功，XXXX执行错误，XXXX访问数据库异常
     * </p>
     *
     * @param message the message to set
     * @since 1.0.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * <p>
     * 获取返回参数信息
     * </p>
     *
     * @return 属性 data 的值。
     * @since 1.0.0
     */
    public T getData() {
        return data;
    }

    /**
     * <p>
     * 设置
     * </p>
     *
     * @param data the data to set
     * @since 1.0.0
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * <p>
     * 获取获取资源id
     * </p>
     *
     * @return 属性 resourceId 的值。
     * @since 1.0.0
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * <p>
     * 设置资源的id
     * </p>
     *
     * @param resourceId the resourceId to set
     * @since 1.0.0
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public static <T> MessageResultBuilder<T> successe(Class<T> clazz) {
        MessageResultBuilder<T> builder = new MessageResultBuilder<>();
        builder.successed(true);
        return builder;
    }

    public static <T> MessageResultBuilder<T> failed(Class<T> clazz) {
        MessageResultBuilder<T> builder = new MessageResultBuilder<>();
        builder.successed(false);
        return builder;
    }

    public static <T> MessageResultBuilder<T> data(T data) {
        MessageResultBuilder<T> builder = new MessageResultBuilder<>();
        return builder.data(data).successed(true).message("操作成功").status(200);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccessed() {
        return isSuccessed;
    }


    public static class MessageResultBuilder<T> {
        private boolean isSuccessed;
        private String message;
        private int status;
        private T data;

        public MessageResultBuilder<T> successed(boolean successed) {
            this.isSuccessed = successed;
            return this;
        }

        public MessageResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public MessageResultBuilder<T> status(int status) {
            this.status = status;
            return this;
        }

        public MessageResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public MessageResult<T> build() {
            MessageResult<T> messageResult = new MessageResult<T>();
            messageResult.setMessage(this.message);
            messageResult.setData((T) this.data);
            messageResult.setSuccessed(this.isSuccessed);
            messageResult.setStatus(status);
            return messageResult;
        }
    }
}
