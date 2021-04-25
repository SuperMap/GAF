package com.supermap.gaf.webgis.cache;


import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;
import com.supermap.gaf.webgis.cache.RegistryResultCacheI;

import java.util.UUID;

/**
 * @author heykb
 */
public abstract class AbstractRegistryResultCache implements RegistryResultCacheI {


    @Override
    public String generateKey() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    @Override
    abstract public BatchRegistryServiceResult get(String code);

    @Override
    abstract public void put(String code,BatchRegistryServiceResult data);

    
    @Override
    public synchronized void success(String code) {
        BatchRegistryServiceResult data = get(code);
        if(data==null){
            data = new BatchRegistryServiceResult();
        }
        data.success();
        put(code,data);
    }

    @Override
    public synchronized void fail(String code) {
        BatchRegistryServiceResult data = get(code);
        if(data==null){
            data = new BatchRegistryServiceResult();
        }
        data.fail();
        put(code,data);
    }

    @Override
    public synchronized void exist(String code) {
        BatchRegistryServiceResult data = get(code);
        if(data==null){
            data = new BatchRegistryServiceResult();
        }
        data.exist();
        put(code,data);
    }

    @Override
    public synchronized void done(String code) {
        BatchRegistryServiceResult data = get(code);
        if(data==null){
            data = new BatchRegistryServiceResult();
        }
        data.done();
        put(code,data);
    }

    @Override
    public synchronized void error(String code) {
        BatchRegistryServiceResult data = get(code);
        if(data==null){
            data = new BatchRegistryServiceResult();
        }
        data.error();
        put(code,data);
    }


}
