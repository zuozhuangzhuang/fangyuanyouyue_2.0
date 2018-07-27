package com.fangyuanyouyue.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fangyuanyouyue.base.util.DateUtil;


/**
 * 后端反馈模板
 * @ClassName ResultUtil
 */
public class ResultUtil {
	//响应状态：0：请求成功，1：请求失败
	private Integer code;
	//响应信息
	private String message;
	//响应时间
	private String date;
	//响应数据
	private Object data;
	//响应结果
	private String report;

	public ResultUtil() {
		super();
		this.code = 1;
		this.message = "请求失败！";
		this.date = new SimpleDateFormat(DateUtil.DATE_FORMT).format(new Date());
	}

	public ResultUtil(Integer code, String message, Date date) {
		super();
		this.code = code;
		this.message = message;
		this.date = new SimpleDateFormat(DateUtil.DATE_FORMT).format(date);
	}

	public ResultUtil(Integer code, String message, Date date, String report) {
		super();
		this.code = code;
		this.message = message;
		this.date = new SimpleDateFormat(DateUtil.DATE_FORMT).format(date);
		this.report = report;
	}

	public ResultUtil(Integer code, String message, Date date, Object data, String report) {
		super();
		this.code = code;
		this.message = message;
		this.date = new SimpleDateFormat(DateUtil.DATE_FORMT).format(date);
		this.data = data;
		this.report = report;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	@Override
	public String toString() {
		return "ResultUtil{" +
				"code=" + code +
				", message='" + message + '\'' +
				", date='" + date + '\'' +
				", data=" + data +
				", report='" + report + '\'' +
				'}';
	}
}
