package com.fangyuanyouyue.forum.dto.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.dto.AppraisalImgDto;
import com.fangyuanyouyue.forum.model.AppraisalDetail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鉴定详情dto
 */
@Getter
@Setter
@ToString
public class AdminAppraisalDetailDto {

    private Integer id;//鉴定id

    private Integer userId;//用户id

    private String nickName;//作者昵称
    
    private String headImgUrl;//作者头像

    private String title;//标题

    private BigDecimal bonus;//赏金

    private String label;//标签

    private Integer sort;//排序

    private Integer status;//状态 1进行中 2结束

    private String addTime; //发布日期
    
    private String endTime;//结束时间

    private String content;//内容
    
    private Integer commentCount = 0;//评论数量
    
    private Integer likesCount = 0;//点赞数量

    private Integer viewCount = 0;//浏览数量
    
    private Integer truthCount = 0;//看真数量
    
    private Integer untruthCount = 0; //看假数量
    
    private List<AppraisalImgDto> imgDtos;//鉴定图片列表
    
    private Integer totalCount = 0;//总数量

    private Integer realCount = 0;//真实数量

    private Integer baseCount = 0;//基础数量


    public AdminAppraisalDetailDto() {
    	
    }


    public AdminAppraisalDetailDto(AppraisalDetail detail) {
        this.id = detail.getId();
        this.userId = detail.getUserId();
        this.title = detail.getTitle();
        this.bonus = detail.getBonus();
        this.label = detail.getLabel();
        this.sort = detail.getSort();
        this.status = detail.getStatus();
        this.endTime = DateUtil.getFormatDate(detail.getEndTime(), DateUtil.DATE_FORMT);
        this.addTime = DateUtil.getFormatDate(detail.getAddTime(), DateUtil.DATE_FORMT);
        this.content = detail.getContent();
        this.nickName = detail.getNickName();
        this.headImgUrl = detail.getHeadImgUrl();
        this.baseCount = detail.getPvCount();
    }

    public static List<AdminAppraisalDetailDto> toDtoList(List<AppraisalDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminAppraisalDetailDto> dtolist = new ArrayList<>();
        for (AppraisalDetail model : list) {
            AdminAppraisalDetailDto dto = new AdminAppraisalDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
