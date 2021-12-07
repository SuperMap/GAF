package com.supermap.gaf.authority.commontype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.gaf.commontypes.tree.ITreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wxl
 * @since 2021/12/1
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典数据树节点")
public class AuthResourceMenuNode extends AuthResourceMenu implements ITreeNode<String,AuthResourceMenuNode> {

    @ApiModelProperty("子菜单")
    private List<AuthResourceMenuNode> children;

    @JsonProperty("isLeaf")
    @ApiModelProperty("是否是叶子节点")
    private boolean leaf;


    @Override
    public String getKey() {
        return super.getResourceMenuId();
    }

    @Override
    public void setKey(String key) {
        super.setResourceMenuId(key);
    }

    @Override
    public String getParentId() {
        return super.getParentId();
    }

    @Override
    public void setParentId(String parentId) {
        super.setParentId(parentId);
    }

    @Override
    public List<AuthResourceMenuNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<AuthResourceMenuNode> children) {
        this.children = children;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
