package com.fangyuanyouyue.goods.dto.adminDto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.GoodsCategory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品类目表
 */
@Getter
@Setter
@ToString
public class AdminGoodsCategoryDto {
    private Integer id;

    private Integer parentId;//上级id
    
    private String parentName; //上级名称

    private String name;//类目名称

    private String imgUrl;//图片地址

    private Integer sort;//排序

    private Integer type;//类型 1普通 2热门

    private Integer status;//状态 0正常 1禁用

    private Integer searchCount = 0;//搜索次数

    private String addTime;//添加时间

    private String updateTime;//更新时间
    

    public AdminGoodsCategoryDto(GoodsCategory model) {
    	this.id = model.getId();
    	this.parentId = model.getParentId();
    	this.parentName = model.getParentName();
    	if(model.getParentName()==null) {
    		this.parentName = "顶级分类";
    	}
    	this.name = model.getName();
    	this.imgUrl = model.getImgUrl();
    	this.sort = model.getSort();
    	this.type = model.getType();
    	this.status = model.getStatus();
    	this.addTime =  DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
    	this.searchCount = model.getSearchCount();
    	this.updateTime =  DateUtil.getFormatDate(model.getUpdateTime(), DateUtil.DATE_FORMT);
    }

    public static List<AdminGoodsCategoryDto> toDtoList(List<GoodsCategory> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminGoodsCategoryDto> dtolist = new ArrayList<>();
        for (GoodsCategory model : list) {
        	AdminGoodsCategoryDto dto = new AdminGoodsCategoryDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}