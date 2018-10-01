package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.SysMsgLog;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统消息记录表dto
 */
@Getter
@Setter
@ToString
public class AdminSysMsgLogDto {
    private Integer id;

    private Integer userId;//发送人id

    private String addTime;//添加时间

    private String content;//消息体

    private String userName;//姓名

    public AdminSysMsgLogDto() {
    }

    public AdminSysMsgLogDto(SysMsgLog sysMsgLog) {
        this.id = sysMsgLog.getId();
        this.userId = sysMsgLog.getUserId();
        this.addTime = DateUtil.getFormatDate(sysMsgLog.getAddTime(),DateUtil.DATE_FORMT);
        this.content = sysMsgLog.getContent();
        this.userName = sysMsgLog.getUserName();
    }


    public static List<AdminSysMsgLogDto> toDtoList(List<SysMsgLog> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminSysMsgLogDto> dtolist = new ArrayList<>();
        for (SysMsgLog model : list) {
            AdminSysMsgLogDto dto = new AdminSysMsgLogDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
