package com.supermap.gaf.storage.enums;

public enum CreatedType {
    CREATED("C"),ALLOCATED("A");
    private String value;

    CreatedType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
