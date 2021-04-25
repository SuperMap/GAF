/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.service.impl;

import com.supermap.gaf.authentication.dao.ThirdPartyAssociationMapper;
import com.supermap.gaf.authentication.entity.entity.po.ThirdPartyAssociation;
import com.supermap.gaf.authentication.service.ThirdPartyAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date:2021/3/25
 * @author : duke
 */
@Service
public class ThirdPartyAssociationServiceImpl implements ThirdPartyAssociationService {
    @Autowired
    private ThirdPartyAssociationMapper thirdPartyAssociationMapper;


    /**
    * @return
    */
    @Override
    public void addThirdPartyAssociation(ThirdPartyAssociation thirdPartyAssociation){
        thirdPartyAssociationMapper.addThirdPartyAssociation(thirdPartyAssociation);
    }

    /**
    * @return
    */
    @Override
    public void deleteThirdPartyAssociation(Integer id){
        thirdPartyAssociationMapper.deleteThirdPartyAssociation(id);
    }

    /**
    * @return
    */
    @Override
    public void updateThirdPartyAssociation(ThirdPartyAssociation thirdPartyAssociation){
        thirdPartyAssociationMapper.updateThirdPartyAssociation(thirdPartyAssociation);
    }

    /**
    * @return
    */
    @Override
    public List<ThirdPartyAssociation> listThirdPartyAssociation(){
        return thirdPartyAssociationMapper.listThirdPartyAssociation();
    }

    /**
    * @return
    */
    @Override
    public ThirdPartyAssociation getThirdPartyAssociation(Integer id){
        return thirdPartyAssociationMapper.getThirdPartyAssociation(id);
    }

    /**
     * @return
     */
    @Override
    public ThirdPartyAssociation getByEnabledThirdPartyAndThirdPartyUserName(String enabledThirdParty, String thirdPartyUsername) {
        return thirdPartyAssociationMapper.getByEnabledThirdPartyAndThirdPartyUserName(enabledThirdParty,thirdPartyUsername);
    }


}
