package com.supermap.gaf.common.storage.entity;

import java.io.Serializable;

/**
 * @author heykb
 */
public class ObjectPartETag implements Serializable {
    private static final long serialVersionUID = 1L;
    private int partNumber;
    private String eTag;

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
