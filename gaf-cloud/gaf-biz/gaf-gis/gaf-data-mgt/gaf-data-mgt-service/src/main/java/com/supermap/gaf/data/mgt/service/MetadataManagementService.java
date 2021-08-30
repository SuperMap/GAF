package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.vo.MetadataVo;

import java.util.List;

public interface MetadataManagementService {
    List<MetadataVo> selectAll();
}
