package com.fangyuanyouyue.base;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fangyuanyouyue.base.enums.ReCode;

/**
 * 控制器基类
 *
 */
public class BaseController {

	protected Logger log = Logger.getLogger(this.getClass());

	/**
	 * 操作成功
	 * 
	 * @return
	 * @throws IOException
	 */
	public BaseResp toSuccess(Object object) {
		BaseResp resp = new BaseResp();
		resp.setCode(ReCode.SUCCESS.getValue());
		resp.setReport(ReCode.SUCCESS.getMessage());
		resp.setData(object);
		return resp;
	}

	/**
	 * 操作成功
	 * 
	 * @return
	 * @throws IOException
	 */
	public BaseResp toSuccess()  {
		BaseResp resp = new BaseResp();
		resp.setCode(ReCode.SUCCESS.getValue());
		resp.setReport(ReCode.SUCCESS.getMessage());
		return resp;
	}

	/**
	 * 操作失败
	 * 
	 * @param reCode
	 * @param reMsg
	 * @return
	 * @throws IOException
	 */
	public BaseResp toError(Integer reCode, String reMsg)  {
		BaseResp resp = new BaseResp();
		resp.setCode(reCode);
		resp.setReport(reMsg);
		return resp;
	}

	/**
	 * 操作失败
	 * 
	 * @param reMsg
	 * @return
	 * @throws IOException
	 */
	public BaseResp toError(String reMsg) {
		return toError(ReCode.FAILD.getValue(), reMsg);
	}

	/**
	 * 操作失败
	 * 
	 * @return
	 * @throws IOException
	 */
	public BaseResp toError() {
		return toError(ReCode.FAILD.getValue(), ReCode.FAILD.getMessage());
	}

}
