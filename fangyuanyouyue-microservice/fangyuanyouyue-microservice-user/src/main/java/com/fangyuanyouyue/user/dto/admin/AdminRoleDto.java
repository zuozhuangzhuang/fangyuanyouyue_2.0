package com.fangyuanyouyue.user.dto.admin;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.user.model.SysRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 */
@Getter
@Setter
@ToString
public class AdminRoleDto {
    private Integer id;

    private String text; 

    private Boolean permission = false; 


    public AdminRoleDto(SysRole model) {
        this.id = model.getId();
        this.text = model.getName();
    }


    public static List<AdminRoleDto> toDtoList(List<SysRole> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminRoleDto> dtolist = new ArrayList<>();
        for (SysRole model : list) {
            AdminRoleDto dto = new AdminRoleDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
