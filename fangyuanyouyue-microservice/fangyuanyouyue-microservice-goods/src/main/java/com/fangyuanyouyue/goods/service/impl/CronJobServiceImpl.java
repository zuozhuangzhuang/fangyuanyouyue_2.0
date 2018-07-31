
package com.fangyuanyouyue.goods.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.BannerIndexMapper;
import com.fangyuanyouyue.goods.dao.GoodsCategoryMapper;
import com.fangyuanyouyue.goods.dao.GoodsCommentMapper;
import com.fangyuanyouyue.goods.dao.GoodsCorrelationMapper;
import com.fangyuanyouyue.goods.dao.GoodsImgMapper;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.dao.HotSearchMapper;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.service.CronJobService;
import com.fangyuanyouyue.goods.service.SchedualUserService;


/**
 * 任务调度
 * @author gao
 *
 */

@Component
public class CronJobServiceImpl implements CronJobService {

	protected Logger log  =  Logger.getLogger(this.getClass());
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapperl;
    @Autowired
    private HotSearchMapper hotSearchMapper;
    @Autowired
    private BannerIndexMapper bannerIndexMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用

	//每分钟刷新 对抢购商品进行更新降价
	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
	@Scheduled(cron="0 * *  * * ? ")
	@Override
	public void updateAuction() {
	    //TODO 定时器每分钟更新需要刷新抢购时间的商品信息
		log.info("-------------------------------------->处理抢购商品<-----------------------------------------------");
		Long nowDate= DateStampUtils.getGMTUnixTimeByCalendar();
		//查询出需要降价的商品
		List<GoodsInfo> goodsInfos = goodsInfoMapper.selectGoodsByIntervalTime(nowDate);
		for(GoodsInfo goodsInfo:goodsInfos){
		    goodsInfo.setPrice(goodsInfo.getPrice().subtract(goodsInfo.getMarkdown()));
            goodsInfo.setLastIntervalTime(DateStampUtils.getTimesteamp());
            goodsInfoMapper.updateByPrimaryKey(goodsInfo);
        }
//		List<AGoods> list = goodsDao.getListByHQL(hql,Type.GOODS_AUCTION.getValue(),Status.GOODS_SELL_ING.getValue(),nowDate);
//		for (AGoods goods:list) {
//			//生成订单
//			AOrder order=new AOrder();
//			List<AGoodsHistory> histories=goodsHistoryDao.getListByHQLPage("select m from AGoodsHistory m where m.AGoods.id=? and m.status=? ",0,1, goods.getId(),Status.AUCTION_ING.getValue());
//			if(histories.size()>0){
//				AGoodsHistory history = histories.get(0);
//				//改变商品状态 出售中0-->已出售2
//				goods.setStatus(Status.GOODS_SELL_OUT.getValue());
//				order.setPrice(history.getPrice());
//				order.setAUser(history.getAUser());
//				order.setPhone(history.getPhone());
//				order.setAddress(history.getAddress());
//				order.setReceiver(history.getReceiver());
//				order.setAddTime(nowDate);
//				order.setAGoods(goods);
//				order.setStatus(Status.ORDER_PREPAY.getValue());
//				order.setType(Type.ORDER_TYPE_GOODS.getValue());
//				order.setDateTime(new Date());
//				order.setContent(goods.getTitle());
//				order.setIsDelete("1");//买家是否删除 默认否1
//				order.setSellDelete("1");//卖家是否删除 默认否1
//				if(goods.getPostage()!=null){
//					order.setPostage(goods.getPostage());
//				}else{
//					order.setPostage(new BigDecimal(0));
//				}
//				if(StringUtils.isNotEmpty(history.getProvince())){
//					order.setProvince(history.getProvince());
//				}
//				if(StringUtils.isNotEmpty(history.getCity())){
//					order.setCity(history.getCity());
//				}
//				if(StringUtils.isNotEmpty(history.getArea())){
//					order.setArea(history.getArea());
//				}
//				history.setStatus(Status.AUCTION_SUCCESS.getValue());
//				goodsHistoryDao.update(history);
//
//		        orderDao.save(order);
//		        String orderNo = addZero(order.getId()+"");
//				order.setOrderNo("AUC"+orderNo);
//				orderDao.update(order);
//
//				//发送系统消息 (给卖家)
//		        appMessageService.sendAppMessageOrder(goods.getAUser().getId(),order.getId(),Status.SELLER.getValue(), "恭喜，您在小方圆上架的竞拍商品【"+goods.getTitle()+"】已被买家拍下!");
//		        //发送系统消息 (给买家)
//		        appMessageService.sendAppMessageOrder(order.getAUser().getId(),order.getId(),Status.BUYER.getValue(), "恭喜，您已成功拍下【"+goods.getTitle()+"】!");
//
//			}else{
//				//改变商品状态 出售中0-->已结束3
//				goods.setStatus(Status.GOODS_SELL_FAIED.getValue());
//				//发送系统消息 (给卖家)
//		        appMessageService.updateSendAppMessage(goods.getAUser().getId(), "您在小方圆上架的拍卖品【"+goods.getTitle()+"】已流拍!");
//			}
//			goodsDao.update(goods);
//		}
//
//		log.info("-------------------------------------->拍卖商品处理完毕<-----------------------------------------------");
	}
//
//	//订单下单30分钟未付款取消(每分钟刷新)
//	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//	@Scheduled(cron="30 * *  * * ? ")
//	@Override
//	public void updateOrder() {
//		log.info("-------------------------------------->每分钟刷新 更改普通商品订单状态<-----------------------------------------------");
//		String hql = "select m from AOrder m  where m.status=? and m.AGoods.isAuction =? and m.addTime < ?  ";
//		//String countHql = "select count(*) from AOrder m where m.status=0 and m.AGoods.isAuction=0 and m.addTime < ? and m.AGoods.status <> 5";
//		Long nowTime=DateStampUtils.getGMTUnixTimeByCalendar();
//
//		long half=60*60*1000/2L;
////		long half=2*60*1000;
//		//long count = goodsDao.countByHql(countHql,nowTime-half);
//		//log.info("需要清理的商品订单数量为："+count);
//		log.info("执行HQL语句："+hql);
//		List<AOrder> list = orderDao.getListByHQL(hql,Status.ORDER_PREPAY.getValue(),Type.GOODS_NORMAL.getValue(),nowTime-half);
//		for(AOrder order:list){
//			order.setStatus(Status.ORDER_CANCEL.getValue());
//			orderDao.update(order);
//			AGoods goods = order.getAGoods();
//			if(goods!=null){
//				//删除下单商品唯一表
//				//uniqueDao.queryHql("delete from AUnique m where m.goodsId=? ", goods.getId());
//				AUnique au=uniqueDao.getByHQL("select m from AUnique m where m.goodsId=?", goods.getId());
//				if(au!=null){
//					uniqueDao.delete(au);
//				}
//
//				goods.setStatus(Status.GOODS_SELL_ING.getValue());
//				goodsDao.update(goods);
//
//				//发送系统消息 (给卖家)
//				appMessageService.updateSendAppMessage(goods.getAUser().getId(), "由于买家未付款，系统已自动取消订单，您的商品【"+goods.getTitle()+"】已重新上架!");
//				//发送系统消息 (给买家)
//				appMessageService.updateSendAppMessage(order.getAUser().getId(), "由于您的订单未及时付款，系统已自动取消订单，订单商品【"+goods.getTitle()+"】");
//
//			}
//		}
//		log.info("-------------------------------------->订单数量为0 状态修改成功<-----------------------------------------------");
//	}
//
//
//	//拍卖商品下单之后 24小时未付款自动取消，并且流拍
//	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//	/*@Scheduled(cron="10 * *  * * ? ")
//	@Override
//	public void updateAuctionOrder() {
//		log.info("-------------------------------------->每分钟刷新 更改普通商品订单状态<-----------------------------------------------");
//		String hql = "select m from AOrder m  where m.status=? and m.AGoods.isAuction=? and m.addTime < ? ";
//		//String countHql = "select count(*) from AOrder m where m.status=0 and m.AGoods.isAuction=1 and m.addTime < ? ";
//		Long nowTime=DateStampUtils.getGMTUnixTimeByCalendar();
//		//long half=2*60*1000;
//		long half=24*60*60*1000;
//		//long count = goodsDao.countByHql(countHql,nowTime-half);
//		//log.info("需要清理的商品订单数量为："+count);
//		log.info("执行HQL语句："+hql);
//		List<AOrder> list = orderDao.getListByHQL(hql,Status.ORDER_PREPAY.getValue(),Type.GOODS_AUCTION.getValue(),nowTime-half);
//		for(AOrder order:list){
//			order.setStatus(Status.ORDER_CANCEL.getValue());
//			orderDao.update(order);
//			AGoods goods=order.getAGoods();
//			if(goods!=null ){
//				goods.setStatus(Status.GOODS_SELL_FAIED.getValue());
//				goodsDao.update(goods);
//				//发送消息提示卖家
//				appMessageService.sendAppMessageGoods(goods.getAUser().getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您的竞价商品：【"+goods.getTitle()+"】由于买家未付款导致已流拍，您可以重新编辑上架。");
//				//发送消息给买家
//				appMessageService.sendAppMessageGoods(goods.getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您购买的竞价商品：【"+goods.getTitle()+"】因您未及时付款，已被系统取消订单。");
//
//			}
//
//		}
//
//		log.info("-------------------------------------->订单数量为0 状态修改成功<-----------------------------------------------");
//	}*/
//
//
//	/**
//	 //拍卖商品下单之后 24小时未付款自动取消
//	 //【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//	 @Scheduled(cron="10 * *  * * ? ")
//	 @Override
//	 public void updateAuctionOrder() {
//	 log.info("-------------------------------------->每分钟刷新 更改普通商品订单状态<-----------------------------------------------");
//	 String hql = "select m from AOrder m  where m.status=? and m.AGoods.isAuction=? and m.addTime < ? ";
//	 //String countHql = "select count(*) from AOrder m where m.status=0 and m.AGoods.isAuction=1 and m.addTime < ? ";
//	 Long nowTime=DateStampUtils.getGMTUnixTimeByCalendar();
//	 //long half=2*60*1000;
//	 long half=24*60*60*1000;
//	 //long count = goodsDao.countByHql(countHql,nowTime-half);
//	 //log.info("需要清理的商品订单数量为："+count);
//	 log.info("执行HQL语句："+hql);
//	 List<AOrder> list = orderDao.getListByHQL(hql,Status.ORDER_PREPAY.getValue(),Type.GOODS_AUCTION.getValue(),nowTime-half);
//	 for(AOrder order:list){
//	 order.setStatus(Status.ORDER_CANCEL.getValue());
//	 orderDao.update(order);
//	 AGoods goods=order.getAGoods();
//	 if(goods!=null ){
//	 //竞拍商品连续被取消订单两次，第二次商品自动流拍
//	 Long cancelCount=goodsHistoryDao.countByHql("select count(*) from AGoodsHistory m where m.AGoods.id=? and m.status = ?", goods.getId(),Status.AUCTION_NOPAY.getValue());
//	 if(cancelCount>=2){
//	 goods.setStatus(Status.GOODS_SELL_FAIED.getValue());
//	 //发送消息提示卖家
//	 appMessageService.sendAppMessageGoods(goods.getAUser().getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您的竞价商品：【"+goods.getTitle()+"】由于买家未付款导致已流拍，您可以重新编辑上架。");
//
//	 }else{
//	 //当前最高出价者出价历史取消订单状态，第二个默认最高
//	 List<AGoodsHistory> goodsHistory=goodsHistoryDao.getListByHQL("select m from AGoodsHistory m where m.AGoods.id=? and m.status <> ? order by m.price desc", order.getAGoods().getId(),Status.AUCTION_NOPAY.getValue());
//	 if(goodsHistory!=null && goodsHistory.size()>0){
//	 Integer removeId=goodsHistory.get(0).getAUser().getId();
//	 if(goodsHistory.size()>1){
//	 //排除相同的用户(第一到第x用户都是同一人)
//	 boolean flag = true;
//	 int index = 0;
//	 for (int j = 0; j < goodsHistory.size()&&flag; j++) {
//	 AGoodsHistory history = goodsHistory.get(j);
//	 if(history.getAUser().getId().intValue()==removeId.intValue()){
//	 history.setStatus(Status.AUCTION_NOPAY.getValue());
//	 goodsHistoryDao.update(history);
//	 index++;
//	 //goodsHistory.remove(j);
//	 }else{
//	 flag = false;
//	 }
//	 }
//	 if(index < goodsHistory.size()){
//	 AGoodsHistory historyTop = goodsHistory.get(index);
//	 historyTop.setStatus(Status.AUCTION_ING.getValue());
//	 goodsHistoryDao.update(historyTop);
//	 //如果竞价商品已经结束，延续7天
//	 goods.setStatus(Status.GOODS_SELL_ING.getValue());
//	 goods.setEndTime(DateStampUtils.getGMTUnixTimeByCalendar()+(long)7*24*60*60*1000);
//	 //goods.setEndTime(DateStampUtils.getGMTUnixTimeByCalendar()+(long)2*60*1000);
//
//	 //发送消息提示卖家
//	 appMessageService.sendAppMessageGoods(goods.getAUser().getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您的竞价商品：【"+goods.getTitle()+"】因买家24小时未付款，现已按上一位竞价者的出价返回竞拍，并为您延时7天。");
//
//
//	 }else{
//	 //如果只有一个人竞拍直接流拍(index > goodsHistory.size())
//	 goods.setStatus(Status.GOODS_SELL_FAIED.getValue());
//	 //发送消息提示卖家
//	 appMessageService.sendAppMessageGoods(goods.getAUser().getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您的竞价商品：【"+goods.getTitle()+"】由于买家未付款导致已流拍，您可以重新编辑上架。");
//
//	 }
//
//	 }else{
//	 //发送消息提示卖家
//	 appMessageService.sendAppMessageGoods(goods.getAUser().getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您的竞价商品：【"+goods.getTitle()+"】由于买家未付款导致已流拍，您可以重新编辑上架。");
//
//	 }
//	 }
//	 }
//	 goodsDao.update(goods);
//	 //发送消息给买家
//	 appMessageService.sendAppMessageGoods(goods.getId(),goods.getId(),Type.GOODS_AUCTION.getValue(), "您购买的竞价商品：【"+goods.getTitle()+"】因您未及时付款，已被系统取消订单。");
//
//	 }
//	 }
//	 log.info("-------------------------------------->订单数量为0 状态修改成功<-----------------------------------------------");
//	 }
//	 **/
//
//
//	//确认发货之后20天自动收货
//	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//
//	@Scheduled(cron="40 * *  * * ? ")
//	@Override
//	public void saveReceiptGoods() {
//		log.info("-------------------------------------->每分钟刷新 自动确认收货<-----------------------------------------------");
//		long count = orderService.updateAutomaticReceiving();
//		log.info("-------------------------------------->订单数量为:"+count+" 确认收货成功<-----------------------------------------------");
//	}
//
//
//	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//	@Scheduled(cron="0 0 0 * * ? ")
//	@Override
//	public void saveDeleteExp() {
//		log.info("-------------------------------------->凌晨清理经验值<-----------------------------------------------");
//		userExpDao.queryHql(" update AUserExp set goodsExp=0 ");
//		log.info("-------------------------------------->凌晨清理经验值成功<-----------------------------------------------");
//	}
//
//
//	//长度少于11位的话前面补0
//	private String addZero(String s){
//		int n = 9;
//		if(s.length()>=n){
//			return s;
//		}
//		return String.format("%0"+(n-s.length())+"d",0)+s;
//	}
//
//	//每小时05分更新一次
//	@Scheduled(cron="0 5 * * * ? ")
//	@Override
//	public void updateWechatAccessToken() {
//		log.info("-------------------------------------->更新微信AccessToken<-----------------------------------------------");
//		try {
//			sysPropertyService.updateWechatAccessToken();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//			log.error("更新微信AccessToken失败");
//		}
//		log.info("-------------------------------------->更新微信AccessToken成功<-----------------------------------------------");
//	}
//
//	//买家申请仅退款后48h自动同意退款
//	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//	@Scheduled(cron="40 * * * * ? ")
//	@Override
//	public void updateOrderRefundTwoDay(){
//		log.info("-------------------------------------->每分钟刷新 自动同意退款<-----------------------------------------------");
//		try{
//			orderService.updateOrderRefundTwoDay();
//		}catch(ServiceException e){
//			e.printStackTrace();
//			log.error("自动同意退款失败");
//		}
//	}
//
//	//买家申请退货退款后72h自动修改订单状态
//	//【秒】   【分】  【时】   【日】  【月】   【周】  【年】  (可选）
//	@Scheduled(cron="40 * * * * ? ")
//	@Override
//	public void updateOrderRefundThreeDay(){
//		log.info("-------------------------------------->每分钟刷新 自动拒绝退款<-----------------------------------------------");
//		try{
//			orderService.updateOrderRefundThreeDay();
//		}catch(ServiceException e){
//			e.printStackTrace();
//			log.error("自动拒绝退款失败");
//		}
//	}
}

