package com.supermap.desktop.develop.entity;

/**
 * @author heykb
 */
public class Catalog {
    private String catalogName;
    private String catalogCode;

    public Catalog(String catalogName, String catalogCode) {
        this.catalogName = catalogName;
        this.catalogCode = catalogCode;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }
}
