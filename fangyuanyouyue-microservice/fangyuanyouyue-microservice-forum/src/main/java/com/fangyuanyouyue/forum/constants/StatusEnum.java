package com.fangyuanyouyue.forum.constants;

public enum StatusEnum {
	
	YES(1),
	NO(2),
	//状态 1显示 2隐藏
	STATUS_NORMAL(1),STATUS_FROZEN(2);

	StatusEnum(Integer value){
		this.value = value;
	}

	private Integer value;

	public Integer getValue() {
		return value;
	}

}
