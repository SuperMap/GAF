package com.supermap.gaf.data.mgt.test;

import com.supermap.gaf.common.storage.client.StorageClient;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.Application;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.resource.DataSourceResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;


@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class DataSourceResourceTest {

    @Autowired
    private DataSourceResource dataSourceResource;

    @Autowired
    @Qualifier("DatamgtStorageClient")
    private StorageClient storageClient;

    @Test
    public void testlistDatasetsById() {
        MessageResult<List<GDataset>> r = dataSourceResource.listDatasetsById("24c2786f-e628-4e67-9a36-64ac38269753");
        r.getData().forEach(gDataset -> {
            System.out.println(gDataset.getDatasetName() + "--" + gDataset.getCaption() + "--" + gDataset.getDatasetType());
        });
        Assert.assertTrue(r.isSuccessed());
    }

    @Test
    public void upload() {
        storageClient.uploadFlie("1.gif", "tenant0", new File("C:\\Users\\kb\\Documents\\ZZDS\\1.gif"));
    }

    @Test
    public void download() {
        storageClient.downloadFile("1.gif", "tenant0", Paths.get("C:\\Users\\kb\\Documents\\ZZDS\\4.gif"));
    }

    @Test
    public void volumePath() {
        VolumePathReturn volumePathReturn = storageClient.getVolumePath("1.gif", "tenant0", false);
        System.out.println(volumePathReturn.getPath());
    }
}