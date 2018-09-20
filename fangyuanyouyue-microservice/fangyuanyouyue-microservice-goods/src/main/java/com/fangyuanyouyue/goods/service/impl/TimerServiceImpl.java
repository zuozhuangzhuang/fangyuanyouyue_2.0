package com.fangyuanyouyue.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.model.GoodsBargain;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.GoodsIntervalHistory;
import com.fangyuanyouyue.goods.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(value = "timerService")
@Transactional(rollbackFor=Exception.class)
public class TimerServiceImpl implements TimerService{
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapper;
    @Autowired
    private HotSearchMapper hotSearchMapper;
    @Autowired
    private BannerIndexMapper bannerIndexMapper;
    @Autowired
    private GoodsQuickSearchMapper goodsQuickSearchMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private CommentLikesMapper commentLikesMapper;
    @Autowired
    private GoodsBargainMapper goodsBargainMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private BargainService bargainService;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private GoodsIntervalHistoryMapper goodsIntervalHistoryMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;

    @Override
    public void depreciate() throws ServiceException {
        //1、获取所有出售中抢购  2、判断当前价格是否高于最低价 3、判断是否超出最后一次降价时间
        // type 类型 1普通商品 2抢购商品
        // status 状态 1出售中 2已售出 3已下架（已结束） 5删除
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectListByTypeStatus(2, 1);
        if(goodsInfos != null && goodsInfos.size()>0){
            for(GoodsInfo goodsInfo:goodsInfos){
                if(goodsInfo.getPrice().compareTo(goodsInfo.getFloorPrice())>=0){
                    //当前价格 > 最低价
                    if(goodsInfo.getLastIntervalTime() == null){
                        //最后一次降价时间为空 = 第一次降价
                        if((goodsInfo.getAddTime().getTime()+goodsInfo.getIntervalTime()*1000) <= System.currentTimeMillis()) {
                            //降价时间间隔
                            long markDown = goodsInfo.getIntervalTime() * 1000;
                            //最后一次降价时间
                            long lastIntervalTime = goodsInfo.getAddTime().getTime();
                            //当前时间
                            Date nowTime = DateStampUtils.getTimesteamp();
                            long nowTime2 = System.currentTimeMillis();
                            long nowTime3 = System.currentTimeMillis() - lastIntervalTime;

                            System.out.println("降价时间间隔：" + markDown);
                            System.out.println("最后一次降价时间：" + lastIntervalTime);
                            System.out.println("当前时间1：" + nowTime.getTime());
                            System.out.println("当前时间2：" + nowTime2);
                            System.out.println("间隔：" + nowTime3);
                            //最后一次降价时间 + 降价时间段 > 当前时间 = 降价
                            //(当前时间-最后一次降价时间)/降价时间间隔=应该降价次数
                            //降价次数*降价幅度
                            long  number =(System.currentTimeMillis() - lastIntervalTime) / markDown;
                            System.out.println("降价次数："+number);
                            addIntervaHistory(goodsInfo, nowTime, number);
                        }
                    }else{
                        //最后一次降价时间 + 降价时间段 <= 当前时间 = 降价
                        if((goodsInfo.getLastIntervalTime().getTime()+goodsInfo.getIntervalTime()*1000) <= System.currentTimeMillis()){
                            long lastIntervalTime = goodsInfo.getLastIntervalTime() == null?goodsInfo.getAddTime().getTime():goodsInfo.getLastIntervalTime().getTime();//最后一次降价时间
                            //降价时间间隔
                            long markDown = goodsInfo.getIntervalTime()*1000;
                            //当前时间
                            Date nowTime = DateStampUtils.getTimesteamp();
                            System.out.println("最后一次降价时间："+lastIntervalTime);
                            System.out.println("降价时间间隔："+markDown);
                            System.out.println("当前时间："+nowTime.getTime());
                            //(当前时间-最后一次降价时间)/降价时间间隔 = 应该降价次数
                            long  number = (nowTime.getTime()-lastIntervalTime)/markDown;
                            System.out.println("降价次数："+number);
                            addIntervaHistory(goodsInfo, nowTime, number);
                        }
                    }
                }/*else if(goodsInfo.getPrice().compareTo(goodsInfo.getFloorPrice()) == 0){
                    if ((goodsInfo.getLastIntervalTime().getTime() + goodsInfo.getIntervalTime() * 1000) <= System.currentTimeMillis()) {
                        //（当前价格 == 最低价 && 最后一次降价时间+降价间隔 <= 当前时间） = 下架
                        goodsInfo.setStatus(3);
                        goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
                    }
                }*/
            }
        }
    }

    private void addIntervaHistory(GoodsInfo goodsInfo, Date nowTime, long  number) {
        for(int i=0;i<number;i++){
            //根据需要降价次数，降价多次
            if(goodsInfo.getPrice().compareTo(goodsInfo.getFloorPrice()) <= 0){
                //如果当前价格与最低价相同，break
                goodsInfo.setStatus(3);
                goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
                return;
            }
            //应该降价数值
            BigDecimal shouldDown = goodsInfo.getMarkdown();
            //降价后价格
            BigDecimal price = goodsInfo.getPrice().subtract(shouldDown);
            if(price.compareTo(goodsInfo.getFloorPrice())<=0){
                //降价后价格 <= 商品最低价
                goodsInfo.setPrice(goodsInfo.getFloorPrice());
            }else{
                goodsInfo.setPrice(price);
            }
            //抢购降价历史
            GoodsIntervalHistory history = new GoodsIntervalHistory();
            history.setGoodsId(goodsInfo.getId());
            history.setAddTime(DateStampUtils.getTimesteamp());
            history.setMarkdown(goodsInfo.getMarkdown());
            history.setPreviousPrice(goodsInfo.getPrice());
            goodsInfo.setLastIntervalTime(nowTime);
            goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
            goodsIntervalHistoryMapper.insert(history);
        }
    }

    @SuppressWarnings("AlibabaAvoidNewDateGetTime")
    @Override
    public void refuseBargain() throws ServiceException {
        //1、获取所有正在申请中的议价 2、根据议价申请时间进行操作 3、通知买家

        //状态 1申请 2同意 3拒绝 4取消
        List<GoodsBargain> goodsBargains = goodsBargainMapper.selectByStatus(1);
        if(goodsBargains != null && goodsBargains.size() > 0){
            for(GoodsBargain bargain:goodsBargains){
                //24h未处理的议价申请就拒绝
                if((System.currentTimeMillis() - bargain.getAddTime().getTime()) > 24*60*60*1000){
//                if((new Date().getTime() - bargain.getAddTime().getTime()) > 3*60*1000){
                    bargain.setStatus(Status.BARGAIN_REFUSE.getValue());//状态 2同意 3拒绝 4取消
                    goodsBargainMapper.updateByPrimaryKey(bargain);
                    //退回余额
                    //调用wallet-service修改余额功能
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(bargain.getUserId(), bargain.getPrice(),Status.ADD.getValue())), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                    //议价：您对商品【商品名称】的议价已被卖家拒绝，点击此处查看详情
                    GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(bargain.getGoodsId());
                    schedualMessageService.easemobMessage(bargain.getUserId().toString(),
                            "您对商品【"+goodsInfo.getName()+"】的议价已被卖家拒绝，点击此处查看详情",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_GOODS.getMessage(),bargain.getGoodsId().toString());
                }
            }
        }
    }
}
