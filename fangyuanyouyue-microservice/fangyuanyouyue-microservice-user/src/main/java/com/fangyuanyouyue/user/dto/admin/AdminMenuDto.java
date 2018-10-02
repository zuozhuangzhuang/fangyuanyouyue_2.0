package com.fangyuanyouyue.user.dto.admin;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.user.model.Feedback;
import com.fangyuanyouyue.user.model.SysMenu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    
    private List<AdminMenuDto> children = new ArrayList<AdminMenuDto>();

    public AdminMenuDto(SysMenu model) {
        this.id = model.getId();
        this.text = model.getName();
        this.icon = model.getIcon();
        this.layer = this.id.toString();
        this.url = this.getUrl();
    }


    public static List<AdminMenuDto> toDtoList(List<SysMenu> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminMenuDto> dtolist = new ArrayList<>();
        for (SysMenu model : list) {
            AdminMenuDto dto = new AdminMenuDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
