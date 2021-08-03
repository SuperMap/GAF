package com.supermap.gaf.common.storage.entity;

import java.util.Date;
import java.util.HashMap;


public class ObjectInfo extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static enum ObjectType {
        commonPrefix, object;
    }

    public static final String NAME_TAG = "name";
    public static final String OBJECT_TYPE_TAG = "objectType";
    public static final String SIZE_TAG = "size";
    public static final String LAST_MODIFIED_TAG = "lastModified";


    public static ObjectInfo fileInfo(String name, Long size, Date lastModified) {
        ObjectInfo re = new ObjectInfo();
        re.setName(name);
        re.setSize(size);
        re.setLastModified(lastModified);
        re.setObjectType(ObjectType.object);
        return re;
    }

    public static ObjectInfo dirInfo(String name) {
        ObjectInfo re = new ObjectInfo();
        re.setName(name);
        re.setObjectType(ObjectType.commonPrefix);
        return re;
    }


    public String getName() {
        return (String) get(NAME_TAG);
    }

    public void setName(String name) {
        put(NAME_TAG, name);
    }

    public ObjectType getObjectType() {
        return (ObjectType) get(OBJECT_TYPE_TAG);
    }

    public void setObjectType(ObjectType objectType) {
        put(OBJECT_TYPE_TAG, objectType);
    }

    public Long getSize() {
        return (Long) get(SIZE_TAG);
    }

    public void setSize(Long size) {
        put(SIZE_TAG, size);
    }

    public Date getLastModified() {
        return (Date) get(LAST_MODIFIED_TAG);
    }

    public void setLastModified(Date lastModified) {
        put(LAST_MODIFIED_TAG, lastModified);
    }
}
