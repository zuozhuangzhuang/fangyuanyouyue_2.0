package com.fangyuanyouyue.forum.constants;

public enum StatusEnum {
	
	YES(1),
	NO(2),
	
	STATUS_NORMAL(1),STATUS_FROZEN(2);

	StatusEnum(Integer value){
		
	}
	
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	
}
