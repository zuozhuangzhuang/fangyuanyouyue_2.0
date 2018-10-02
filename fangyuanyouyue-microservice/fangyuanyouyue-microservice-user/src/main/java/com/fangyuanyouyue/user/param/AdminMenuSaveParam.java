package com.fangyuanyouyue.user.param;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 */
@Getter
@Setter
@ToString
public class AdminMenuSaveParam {
    private Integer id;

    private String text; 

    private String icon; 

    private Integer tenantId; 

    private String layer; 

    private String url;
    
    private String type;
    
    private List<AdminMenuSaveParam> children;

	//权限
	private List<AdminRoleParam> auth;

    public AdminMenuSaveParam() {
    	
    }



}
