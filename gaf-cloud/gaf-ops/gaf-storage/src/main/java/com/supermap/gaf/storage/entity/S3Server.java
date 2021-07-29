package com.supermap.gaf.storage.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.validation.constraints.*;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("")
public class S3Server implements Serializable{
    @NotNull
    @ApiModelProperty("")
    private String accessKey;
    @NotNull
    @ApiModelProperty("")
    private String secretKey;
    @NotNull
    @ApiModelProperty("")
    private String serviceEndpoint;
    /**
    * 默认值1：NULL::character varying
    */
    @ApiModelProperty("")
    private String id;
}