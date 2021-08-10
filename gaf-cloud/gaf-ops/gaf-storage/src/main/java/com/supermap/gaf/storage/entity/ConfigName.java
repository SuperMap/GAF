package com.supermap.gaf.storage.entity;

import java.util.HashMap;

public class ConfigName extends HashMap<String, Object> {
    public static final String NAME_TAG = "name";

    public ConfigName(String name) {
        put(NAME_TAG, name);
    }

    public String getName() {
        return (String) get(NAME_TAG);
    }

    public void setName(String name) {
        put(NAME_TAG, name);
    }

}
