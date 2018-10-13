package com.fangyuanyouyue.user.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 */
@Getter
@Setter
@ToString
public class AdminOperatorParam {
	
    private Integer id;

    private Integer[] roleIds;

    private Integer userId;
    
    private Integer roleId;
    
    private String password;
    
    private String confirm;
    
    private String userCode;
    
    private String state;

    public AdminOperatorParam() {
    	
    }



}
