/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wxl
 * @since 2021/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicsResult {
    List<PhysicsSingleResult> successed;
    List<PhysicsSingleResult> failed;
}
