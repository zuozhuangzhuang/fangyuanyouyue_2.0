package com.fangyuanyouyue.wallet.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.wallet.constant.StatusEnum;
import com.fangyuanyouyue.wallet.constant.TypeEnum;
import com.fangyuanyouyue.wallet.dao.CouponInfoMapper;
import com.fangyuanyouyue.wallet.dao.PointGoodsMapper;
import com.fangyuanyouyue.wallet.dao.PointOrderMapper;
import com.fangyuanyouyue.wallet.dao.UserCouponMapper;
import com.fangyuanyouyue.wallet.model.CouponInfo;
import com.fangyuanyouyue.wallet.model.PointGoods;
import com.fangyuanyouyue.wallet.model.PointOrder;
import com.fangyuanyouyue.wallet.model.UserCoupon;
import com.fangyuanyouyue.wallet.service.PointOrderService;
import com.fangyuanyouyue.wallet.service.WalletService;

@Service(value = "pointOrderService")
@Transactional
public class PointOrderServiceImp implements PointOrderService{

    @Autowired
    private PointGoodsMapper pointGoodsMapper;
    @Autowired
    private PointOrderMapper pointOrderMapper;
    @Autowired
    private CouponInfoMapper couponInfoMapper;
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private WalletService walletService;

	@Override
	public void saveOrder(Integer userId, Integer goodsId) throws ServiceException {
		PointGoods goods = pointGoodsMapper.selectByPrimaryKey(goodsId);
		if(goods==null) {
			throw new ServiceException("商品不存在或已下架");
		}
		
		//判断库存
		CouponInfo coupon = couponInfoMapper.selectByPrimaryKey(goods.getCouponId());
		if(coupon==null||coupon.getCount()<=0) {
			throw new ServiceException("该商品已经被抢光啦，再次早点来哦");
		}

		//TODO 1 减库存 2 扣除积分 3 保存订单信息 4 保存用户优惠券
		Integer count = coupon.getCount();
		coupon.setCount(count-1);
		couponInfoMapper.updateByPrimaryKey(coupon);
		
		//扣除积分
		walletService.updateScore(userId,goods.getPoint(), 2);
		
		//保存订单信息
		PointOrder order = new PointOrder();
		order.setAddTime(new Date());
		order.setUserId(userId);
		order.setDescription(goods.getDescription());
		order.setGoodsId(goodsId);
		order.setGoodsName(goods.getName());
		order.setMainImgUrl(goods.getCoverImgUrl());
		order.setPoint(goods.getPoint());
		order.setType(TypeEnum.VRITUAL_GOODS.getCode());
        //订单号
        final IdGenerator idg = IdGenerator.INSTANCE;
        String id = idg.nextId();
        order.setOrderNo(id);
        
		pointOrderMapper.insert(order);
		
		//保存用户优惠券
		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setAddTime(new Date());
		userCoupon.setCouponId(coupon.getId());
		userCoupon.setStatus(StatusEnum.COUPON_NOTUSE.getCode());
		userCoupon.setUserId(userId);
		userCouponMapper.insert(userCoupon);
		
		
	}
   
}
