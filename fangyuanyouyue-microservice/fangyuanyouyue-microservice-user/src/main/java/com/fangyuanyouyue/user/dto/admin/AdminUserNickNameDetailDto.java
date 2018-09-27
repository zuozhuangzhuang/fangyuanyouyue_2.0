package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserNickNameDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户修改昵称记录表Dto
 */
@Getter
@Setter
@ToString
public class AdminUserNickNameDetailDto {

    private Integer userId;//用户id

    private String oldNickName;//修改前昵称

    private String newNickName;//修改后昵称

    private String addTime;//添加时间

    private String nickName;//用户当前昵称

    public AdminUserNickNameDetailDto() {
    }

    public AdminUserNickNameDetailDto(UserNickNameDetail detail) {
        this.userId = detail.getUserId();
        this.oldNickName = detail.getOldNickName();
        this.newNickName = detail.getNewNickName();
        this.addTime = DateUtil.getFormatDate(detail.getAddTime(),DateUtil.DATE_FORMT);
        this.nickName = detail.getNickName();
    }


    public static List<AdminUserNickNameDetailDto> toDtoList(List<UserNickNameDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminUserNickNameDetailDto> dtolist = new ArrayList<>();
        for (UserNickNameDetail model : list) {
            AdminUserNickNameDetailDto dto = new AdminUserNickNameDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
