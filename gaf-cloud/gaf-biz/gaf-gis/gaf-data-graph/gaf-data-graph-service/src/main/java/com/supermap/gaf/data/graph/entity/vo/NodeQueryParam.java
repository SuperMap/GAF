package com.supermap.gaf.data.graph.entity.vo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Map;

/**
 * @author : duke
 * @since 2021/5/10 2:49 PM
 */
@Data
public class NodeQueryParam {
    private String label;
    private Map<String,Object> attribute;


    public String getJsonFormatAttribute(){
        String res = JSON.toJSONString(attribute);
        //去掉key的双引号
        for (String key : attribute.keySet()){
            res = res.replaceAll("\""+key+"\"",key);
        }
        return res;
    }
}
