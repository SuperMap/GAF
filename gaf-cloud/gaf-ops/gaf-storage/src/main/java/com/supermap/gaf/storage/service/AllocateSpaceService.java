package com.supermap.gaf.storage.service;

import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.SpaceConfig;
import com.supermap.gaf.storage.utils.Page;

public interface AllocateSpaceService {
    Page<Space> select(String spaceId, Integer pageNum, Integer pageSize);

    void allocate(Space space);
}
