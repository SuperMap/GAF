/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.dao;

import com.supermap.gaf.authentication.entity.entity.po.ThirdPartyAssociation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date:2021/3/25
 * @author : duke
 */
@Component
public interface ThirdPartyAssociationMapper {
    /**
     * 添加第三方关联
     * @param thirdPartyAssociation
     */
    void addThirdPartyAssociation(ThirdPartyAssociation thirdPartyAssociation);

    /**
     * 删除第三方关联
     * @param id
     */
    void deleteThirdPartyAssociation(Integer id);

    /**
     * 编辑第三方关联
     * @param thirdPartyAssociation
     */
    void updateThirdPartyAssociation(ThirdPartyAssociation thirdPartyAssociation);

    /**
     * 查询第三方关联
     * @return
     */
    List<ThirdPartyAssociation> listThirdPartyAssociation();

    /**
     * 根据id查询第三方关联
     * @param id
     * @return
     */
    ThirdPartyAssociation getThirdPartyAssociation(Integer id);

    /**
     * 根据条件筛选第三方关联
     * @param enabledThirdParty
     * @param thirdPartyUsername
     * @return
     */
    ThirdPartyAssociation getByEnabledThirdPartyAndThirdPartyUserName(@Param("enabledThirdParty") String enabledThirdParty, @Param("thirdPartyUsername") String thirdPartyUsername);
}
