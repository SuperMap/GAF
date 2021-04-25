package com.supermap.gaf.commontypes;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用于分页查询的基类
 * </p>
 * @author ${Author}
 * @version ${Version}
 * @since 1.0.0
 *
 */
@ApiModel(description= "分页查询的基类")
public abstract class  SearchParameter implements Serializable {

    /**
     * <p>
     * 
     * </p>
     * @since 1.0.0
     */
    private static final long serialVersionUID = 4721763449794897096L;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@ApiModelProperty(value = "统一的模糊查询关键字")
    private String keyword = "";
    
    @ApiModelProperty(value = "页面规格")
    private int pageSize = 10;
	
    @ApiModelProperty(value = "页面索引")
	private int pageIndex = 1;
    
    @ApiModelProperty(value = "排序的枚举类型，包括ASC、 DESC")
	private OrderType orderType;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public OrderType getOrderType() {
		if(orderType == null){
			return OrderType.ASC;
		}
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public static enum OrderType{
	    /**
	     * 递序
	     */
	    ASC, 
	    /**
	     * 降序
	     */
	    DESC
	}
    
}
