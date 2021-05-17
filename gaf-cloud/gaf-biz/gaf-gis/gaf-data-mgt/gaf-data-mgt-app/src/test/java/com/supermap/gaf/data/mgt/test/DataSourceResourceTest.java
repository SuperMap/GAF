package com.supermap.gaf.data.mgt.test;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.Application;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.resource.DataSourceResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class DataSourceResourceTest {

    @Autowired
    private DataSourceResource dataSourceResource;


    @Test
    public void testlistDatasetsById() {
        MessageResult<List<GDataset>> r = dataSourceResource.listDatasetsById("24c2786f-e628-4e67-9a36-64ac38269753");
        r.getData().forEach(gDataset -> {
            System.out.println(gDataset.getDatasetName()+"--"+gDataset.getCaption()+ "--"+ gDataset.getDatasetType());
        });
        Assert.assertTrue(r.isSuccessed());
    }
}