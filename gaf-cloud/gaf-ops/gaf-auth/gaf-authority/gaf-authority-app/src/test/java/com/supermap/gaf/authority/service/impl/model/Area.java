/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wxl
 * @date:2021/3/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Area {
    private Integer level;
    private String nameabbrevation;
    private String name;
    private String adminType;
    private String cityCode;
    private double lnt;
    private String englishabbrevation;
    private String english;
    private String bound;
    private double lat;
    private List<Area> child;
    private List<AreaOmit> parents;
}
