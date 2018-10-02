package com.fangyuanyouyue.user.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 */
@Getter
@Setter
@ToString
public class AdminRoleParam {
	
    private Integer id;

    private Integer menuId;
    
    private String roleName;
    
    private String roleAuth;

    public AdminRoleParam() {
    	
    }



}
