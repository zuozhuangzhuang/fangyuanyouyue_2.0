
package com.fangyuanyouyue.goods.service;


/**
 * 任务调度
 *
 */
public interface CronJobService {

	/**
	 * 每分钟刷新 更改抢购商品状态
	 */

	public void updateAuction();


	/**
	 * 30分钟刷新 更改商品订单订单状态
	 */

//	public void updateOrder();


	/**
	 * 30分钟刷新 更改拍卖订单状态
	 */

//	public void updateAuctionOrder();


	/**
	 * 12天自动收货
	 */

//	public void saveReceiptGoods();



	/**
	 *
	 * 清空分享经验值
	 */

//	public void saveDeleteExp();


	/**
	 *
	 * 更新微信AccessToken
	 */

//	public void updateWechatAccessToken();

	/**
	 * 仅退款：48小时卖家未处理自动退款
	 */
//	public void updateOrderRefundTwoDay();

	/**
	 * 退货退款：72小时卖家未处理自动交给后台处理
	 */
//	public void updateOrderRefundThreeDay();

}

