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
 */
@Getter
@Setter
@ToString
public class AdminMenuDto {
    private Integer id;

    private String text; 

    private String icon; 

    private Integer tenantId = 8; 

    private String layer; 

    private String url;
    
    private List<AdminMenuDto> children;

    public AdminMenuDto(Feedback model) {
        this.id = model.getId();
    }


    public static List<AdminMenuDto> toDtoList(List<Feedback> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminMenuDto> dtolist = new ArrayList<>();
        for (Feedback model : list) {
            AdminMenuDto dto = new AdminMenuDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
