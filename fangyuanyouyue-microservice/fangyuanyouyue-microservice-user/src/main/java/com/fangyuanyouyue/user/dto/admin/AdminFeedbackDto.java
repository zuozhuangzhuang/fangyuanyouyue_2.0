package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.Feedback;
import com.fangyuanyouyue.user.model.UserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户反馈信息dto
 */
@Getter
@Setter
@ToString
public class AdminFeedbackDto {
    private Integer id;//反馈信息id

    private Integer userId;//用户id

    private String addTime;//新增时间

    private String version;//版本

    private Integer type;//类型 1安卓 2iOS 3小程序

    private String content;//反馈内容

    private String nickName;//昵称

    public AdminFeedbackDto(Feedback model) {
        this.id = model.getId();
        this.userId = model.getUserId();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(),DateUtil.DATE_FORMT);
        this.version = model.getVersion();
        this.type = model.getType();
        this.content = model.getContent();
        this.nickName = model.getNickName();
    }


    public static List<AdminFeedbackDto> toDtoList(List<Feedback> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminFeedbackDto> dtolist = new ArrayList<>();
        for (Feedback model : list) {
            AdminFeedbackDto dto = new AdminFeedbackDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
