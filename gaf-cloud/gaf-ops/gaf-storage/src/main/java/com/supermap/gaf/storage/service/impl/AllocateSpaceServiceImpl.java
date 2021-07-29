package com.supermap.gaf.storage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.storage.dao.SpaceMapper;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.vo.SpaceSelectVo;
import com.supermap.gaf.storage.enums.CreatedType;
import com.supermap.gaf.storage.service.AllocateSpaceService;
import com.supermap.gaf.storage.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllocateSpaceServiceImpl implements AllocateSpaceService {

    @Autowired
    private SpaceMapper spaceMapper;

    @Override
    public Page<Space> select(String spaceId, Integer pageNum, Integer pageSize) {
        PageInfo<Space> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            spaceMapper.selectList(SpaceSelectVo.builder().target(spaceId).createdType(CreatedType.ALLOCATED.getValue()).build());
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public void allocate(Space space) {
        Space parent = spaceMapper.select(space.getParentSpaceId());
        if(space!=null){
            space.setCreatedType(CreatedType.ALLOCATED.getValue());
            spaceMapper.insert(space);
        }
    }
}
