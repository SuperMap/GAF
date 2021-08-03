package com.supermap.gaf.storage.enums;

public enum TargetType {
    PLATFORM("P"), TENANT("T");
    private String value;

    TargetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
