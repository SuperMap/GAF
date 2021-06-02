/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.supermap.gaf.authority.Application;
import com.supermap.gaf.authority.service.impl.model.Area;
import com.supermap.gaf.authority.service.impl.model.R;
import com.supermap.gaf.sys.mgt.commontype.SysDict;
import com.supermap.gaf.sys.mgt.dao.SysDictMapper;
import com.supermap.gaf.utils.MybatisBatchUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @date:2021/3/25
 * @author wxl
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class TianDiTuSpiderTest {

    @Autowired
    private RestTemplate restTemplate;

    private Map<String,Object> map = new HashMap<>();

    //@Test
    //public void testPickUp() {
    //    Map<String, Object> requestMap = new HashMap<>();
    //    requestMap.put("postStr", "{\"searchWord\":\"中华人民共和国\",\"searchType\":\"1\",\"needSubInfo\":\"false\",\"needAll\":\"true\",\"needPre\":\"true\"}");
    //    String  areaStr = restTemplate.getForObject("http://api.tianditu.gov.cn/administrative?postStr={postStr}&tk=83c663bd5e7a96b4117636ef5de266b1", String.class,requestMap);
    //    R<List<Area>> r = JSONObject.parseObject(areaStr, new TypeReference<R<List<Area>>>() {
    //    });
    //    List<Area> areas = r.getData();
    //    List<SysDict> sysDicts = convert(areas);
    //    MybatisBatchUtil.insertOrUpdateBatch(SysDictMapper.class,sysDicts, SysDictMapper::insert);
    //}

    private List<SysDict> convert(List<Area> areas) {
        List<SysDict> all = new LinkedList<>();
        Area china = areas.get(0);// 中国行政区划
        List<Area> allProvince = china.getChild();
        for (int i = 0; i < allProvince.size(); i++) {
            // 遍历省
            Area province = allProvince.get(i);
            SysDict provinceDict = new SysDict();
            provinceDict.setDataDictId(UUID.randomUUID().toString());
            areaConvertToSysDict(province,provinceDict);
            provinceDict.setPid("5dceaa79-0394-4fc2-810c-4e48d61e8759");
            provinceDict.setSeq(i+1);
            all.add(provinceDict);
            List<Area> citys = province.getChild();
            if(citys == null || citys.isEmpty() ) continue;
            for (int j = 0; j < citys.size(); j++) {
                // 遍历市
                Area city = citys.get(j);
                SysDict cityDict = new SysDict();
                cityDict.setDataDictId(UUID.randomUUID().toString());
                areaConvertToSysDict(city,cityDict);
                cityDict.setPid(provinceDict.getDataDictId());
                cityDict.setSeq(j+1);
                all.add(cityDict);
                List<Area> regions = city.getChild();
                if(regions == null || regions.isEmpty() ) continue;
                for (int k = 0; k < regions.size(); k++) {
                    // 遍历区
                    Area region = regions.get(k);
                    SysDict regionDict = new SysDict();
                    regionDict.setDataDictId(UUID.randomUUID().toString());
                    areaConvertToSysDict(region,regionDict);
                    regionDict.setPid(cityDict.getDataDictId());
                    regionDict.setSeq(k+1);
                    all.add(regionDict);
                }
            }
        }
        return all;
    }
    private void areaConvertToSysDict(Area area,SysDict sysDict) {
        sysDict.setTenantId("tenant_000000");
        sysDict.setDictCode("XZQH");
        sysDict.setStatus(true);
        sysDict.setDictName(area.getName());
        sysDict.setVisibility(true);
        sysDict.setCreatedBy("sys_admin");
        sysDict.setUpdatedBy("sys_admin");
        sysDict.setDictValue(area.getCityCode());
        map.put("level", area.getLevel());
        map.put("nameabbrevation",area.getNameabbrevation());
        map.put("adminType", area.getAdminType());
        map.put("lnt", area.getLnt());
        map.put("englishabbrevation",area.getEnglishabbrevation());
        map.put("english",area.getEnglish());
        map.put("bound",area.getBound());
        map.put("lat",area.getLat());
        sysDict.setExtProperties(JSONObject.toJSONString(map));
    }

}
