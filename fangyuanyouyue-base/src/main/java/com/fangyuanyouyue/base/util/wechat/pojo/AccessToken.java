package com.fangyuanyouyue.base.util.wechat.pojo;

/**
 * 微信通用接口凭证
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class AccessToken {
	
	private String token;// 获取到的凭证
	private int expiresIn;// 凭证有效时间，单位：秒
	private long expiresLong;
	
	private String ticket;// 获取到的凭证
	private int TicketExpiresIn;// 凭证有效时间，单位：秒
	private long TicketExpiresLong;
	
	private Integer errcode;
	private String errmsg;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getTicketExpiresIn() {
		return TicketExpiresIn;
	}

	public void setTicketExpiresIn(int ticketExpiresIn) {
		TicketExpiresIn = ticketExpiresIn;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public long getExpiresLong() {
		return expiresLong;
	}

	public void setExpiresLong(long expiresLong) {
		this.expiresLong = expiresLong;
	}

	public long getTicketExpiresLong() {
		return TicketExpiresLong;
	}

	public void setTicketExpiresLong(long ticketExpiresLong) {
		TicketExpiresLong = ticketExpiresLong;
	}

	@Override
	public String toString() {
		return "AccessToken [token=" + token + ", expiresIn=" + expiresIn
				+ ", expiresLong=" + expiresLong + ", ticket=" + ticket
				+ ", TicketExpiresIn=" + TicketExpiresIn
				+ ", TicketExpiresLong=" + TicketExpiresLong + ", errcode="
				+ errcode + ", errmsg=" + errmsg + "]";
	}


	
}