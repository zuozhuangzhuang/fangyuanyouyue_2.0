package com.fangyuanyouyue.forum.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.constants.StatusEnum;
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
public class AppraisalDetailDto {

    private Integer appraisalId;//鉴定id

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
    
    private Integer isCollect = StatusEnum.NO.getValue(); //是否收藏 1是 2否
    
    private Integer isLikes = StatusEnum.NO.getValue(); //是否点赞 1是 2否

//    private Integer isFans = StatusEnum.NO.getValue(); //是否关注作者

    private List<String> headImgUrls;//参与鉴定列表，最多五个，按照热度排序

    private List<AppraisalImgDto> imgDtos;//鉴定图片列表

    //以下为我参与的、我发起的中包含数据
    private Integer viewpoint;//评论观点 1看真 2看假（如果为null则未登录或未发表看法，提交评论需先判断为null方可）

    private Integer isWinner = 2; //是否赢了 1是 2否

    public AppraisalDetailDto() {
    	
    }


    public AppraisalDetailDto(AppraisalDetail detail) {
        this.appraisalId = detail.getId();
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
    }

    public static List<AppraisalDetailDto> toDtoList(List<AppraisalDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AppraisalDetailDto> dtolist = new ArrayList<>();
        for (AppraisalDetail model : list) {
            AppraisalDetailDto dto = new AppraisalDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
