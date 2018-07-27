package com.fangyuanyouyue.base.enums;

/**
 * 状态
 *
 */
public enum Status {
	
	YES("0"),NO("1"),
	//收货地址类型 1默认地址 2其他
	ISDEFAULT("1"),OTHER("2"),
	NORMAL("0"),UNNORMAL("1"),DELETE("2"),	//普通的状态，只有两种
	//状态 0 待付款  1已付款  2已完成  3已发货  4已取消
	ORDER_PREPAY("0"),ORDER_PAY("1"),ORDER_COMPLETE("2"),ORDER_SENDED("3"),ORDER_CANCEL("4"),
	
	//竞拍状态 0竞拍结束   1正在竞拍(当前最高价) 2拍卖成功 商品竞拍结束或者有更高的出价的时候 当前用户的竞拍结束 3竞价成功没有付款
	AUCTION_END("0"),AUCTION_ING("1"),AUCTION_SUCCESS("2"),AUCTION_NOPAY("3"),
	
	//拍卖商品状态 0未开始   1拍卖中  2已售出  3已结束(无人竞拍)   普通商品状态  1出售中，2 已售出  5删除
	GOODS_NOT_BEGIN("0"),GOODS_SELL_ING("1"),GOODS_SELL_OUT("2"),GOODS_SELL_FAIED("3"),GOODS_DELETE("5"),
	
	//状态 0 审核中 1审核通过 2审核拒绝
	WITHDRAW_APPLY("0"),WITHDRAW_SUCCESS("1"),WITHDRAW_REJECT("2"),

	//退货
	//状态 0 审核中 1审核通过 2审核拒绝 （官方状态）
	ORDER_RETURN_APPLY("1"),ORDER_RETURN_SUCCESS("2"),ORDER_RETURN_REJECT("3"),
	//状态 卖家是否同意退货状态 null正常  1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货 （卖家状态）
	SELLER_RETURN_APPLY("1"),SELLER_RETURN_SUCCESS("2"),SELLER_RETURN_REJECT("3"),SELLER_RETURN_DEFAULT_SUCCESS("4"),SELLER_RETURN_DEFAULT_REJECT("5"),

	//readed是否已读 0是1否
	READED_YES("0"),READED_NO("1"),
	
	WECHAT_REGISTER_NOPHONE("0"),WECHAT_REGISTER_HAVEPHONE("1"),

	//区分买家卖家 买家
	BUYER("0"),SELLER("1"),
	//区分被限制用户，0 被限制，1 解除限制
	CONFINED("0"),RELIEVE("1"),
	//0普通商品 1拍卖商品
	GOODS("0"),AUCTION("1"),
	;

	private final String value;

	Status(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	

}
