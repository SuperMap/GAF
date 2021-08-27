/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.model;

import com.supermap.gaf.data.mgt.entity.MmPhysics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单表物理化结果
 * @author wxl
 * @since 2021/8/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicsSingleResult {
    boolean success;
    String message;
    MmPhysics mmPhysics;
}
