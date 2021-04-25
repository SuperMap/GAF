package com.supermap.gaf.data.access.commontypes;

/**
 * @author heykb
 */
public class RevisionSortSnParam {
    private String tableName;
    private String parentIdFieldName;
    private String parentId ;
    private String idFieldName;
    private String sortSnFieldName;
    private String updatedTimeFieldName;
    private String logicDeleteFieldName;
    private Integer oldSortSn;
    private Integer curSortSn;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getParentIdFieldName() {
        return parentIdFieldName;
    }

    public void setParentIdFieldName(String parentIdFieldName) {
        this.parentIdFieldName = parentIdFieldName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getSortSnFieldName() {
        return sortSnFieldName;
    }

    public void setSortSnFieldName(String sortSnFieldName) {
        this.sortSnFieldName = sortSnFieldName;
    }

    public String getUpdatedTimeFieldName() {
        return updatedTimeFieldName;
    }

    public void setUpdatedTimeFieldName(String updatedTimeFieldName) {
        this.updatedTimeFieldName = updatedTimeFieldName;
    }

    public String getLogicDeleteFieldName() {
        return logicDeleteFieldName;
    }

    public void setLogicDeleteFieldName(String logicDeleteFieldName) {
        this.logicDeleteFieldName = logicDeleteFieldName;
    }

    public Integer getOldSortSn() {
        return oldSortSn;
    }

    public void setOldSortSn(Integer oldSortSn) {
        this.oldSortSn = oldSortSn;
    }

    public Integer getCurSortSn() {
        return curSortSn;
    }

    public void setCurSortSn(Integer curSortSn) {
        this.curSortSn = curSortSn;
    }
}
