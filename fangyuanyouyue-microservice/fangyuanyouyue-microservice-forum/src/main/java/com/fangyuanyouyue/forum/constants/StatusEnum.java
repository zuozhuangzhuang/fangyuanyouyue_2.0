package com.fangyuanyouyue.forum.constants;

public enum StatusEnum {
	
	STATUS_NORMAL(1),STATUS_FROZEN(2);

	StatusEnum(Integer value){
		
	}
	
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	
}
