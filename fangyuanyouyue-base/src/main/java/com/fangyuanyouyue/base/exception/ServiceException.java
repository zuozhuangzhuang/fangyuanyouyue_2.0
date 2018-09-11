package com.fangyuanyouyue.base.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 业务处理异常
 */
@Getter
@Setter
@ToString
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	private Integer code = 1;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Integer code,String msg) {
		super(msg);
		setCode(code);
	}
}
