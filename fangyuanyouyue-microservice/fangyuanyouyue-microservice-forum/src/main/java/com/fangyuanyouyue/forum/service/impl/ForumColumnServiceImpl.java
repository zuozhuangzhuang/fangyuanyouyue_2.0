package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.forum.dao.*;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.dto.MyColumnDto;
import com.fangyuanyouyue.forum.model.ColumnOrder;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.model.ForumColumnApply;
import com.fangyuanyouyue.forum.model.ForumColumnType;
import com.fangyuanyouyue.forum.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service(value = "forumColumnService")
public class ForumColumnServiceImpl implements ForumColumnService {

    @Autowired
    private ForumColumnMapper forumColumnMapper;
    @Autowired
	private ForumColumnTypeMapper forumColumnTypeMapper;
    @Autowired
	private SchedualMessageService schedualMessageService;
    @Autowired
	private SchedualUserService schedualUserService;
    @Autowired
	private SchedualWalletService schedualWalletService;
    @Autowired
	private ForumColumnApplyMapper forumColumnApplyMapper;
    @Autowired
	private ColumnOrderMapper columnOrderMapper;
    @Autowired
	private ForumInfoService forumInfoService;
    @Autowired
	private ForumPvMapper forumPvMapper;

	@Override
	public List<ForumColumnTypeDto> getColumnList(Integer start, Integer limit) throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectPage(start*limit, limit);
		List<ForumColumnDto> dtos = ForumColumnDto.toDtoList(list);
		return ForumColumnTypeDto.toDtoList(dtos);
	}

	@Override
	public List<ForumColumnDto> getChosenColumnList(Integer start,Integer limit) throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectChosen(start*limit,limit,1);
		return ForumColumnDto.toDtoList(list);
	}


	@Override
	public List<ForumColumnTypeDto> getForumTypeList() throws ServiceException {
		List<ForumColumnType> typeList = forumColumnTypeMapper.getAll();
		return ForumColumnTypeDto.toDtoListByType(typeList);
	}

	@Override
	public Object addColumn(Integer userId, Integer typeId,String name,Integer payType,String payPwd) throws ServiceException {
		//如果已申请的用户超过50人，就必须要存在payType，前50人申请不需要支付
		//name已存在的申请时返回
		if(forumColumnMapper.selectByName(name)!=null){
			throw new ServiceException("专栏名已存在！");
		}
		ForumColumnType forumColumnType = forumColumnTypeMapper.selectByPrimaryKey(typeId);
		if(forumColumnType == null){
			throw new ServiceException("分类异常！");
		}else{
			ForumColumn forumColumn = forumColumnMapper.selectByUserId(userId);
			if(forumColumn != null){
				throw new ServiceException("每位用户只能申请一个专栏！");
			}else{
				ColumnOrder columnOrder = new ColumnOrder();
				columnOrder.setAmount(new BigDecimal(200));
				columnOrder.setUserId(userId);
				columnOrder.setStatus(1);//状态 1待支付 2已完成 3已删除
				columnOrder.setAddTime(DateStampUtils.getTimesteamp());
				columnOrder.setTypeId(typeId);
				columnOrder.setName(name);
				//订单号
				final IdGenerator idg = IdGenerator.INSTANCE;
				String id = idg.nextId();
				columnOrder.setOrderNo(id);
				columnOrderMapper.insert(columnOrder);

				StringBuffer payInfo = new StringBuffer();
				//支付
				if(payType.intValue() == 1){//微信,如果回调失败就不做处理，成功就在回调接口中继续生成全民鉴定
					WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechat(columnOrder.getOrderNo(), columnOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.column_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
					return wechatPayDto;
				}else if(payType.intValue() == 2){//支付宝,如果回调失败就不做处理，成功就在回调接口中继续生成全民鉴定
					String info = JSONObject.parseObject(schedualWalletService.orderPayByALi(columnOrder.getOrderNo(), columnOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.column_alipay_notify.getNotifUrl())).getString("data");
					payInfo.append(info);
				}else if(payType.intValue() == 3) {//余额
					//验证支付密码
					Boolean verifyPayPwd = JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getBoolean("data");
					if (!verifyPayPwd) {
						throw new ServiceException("支付密码错误！");
					} else {
						//调用wallet-service修改余额功能,成为栏主200元/人
						BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(userId, new BigDecimal(200), 2)), BaseResp.class);
						if (baseResp.getCode() == 1) {
							throw new ServiceException(baseResp.getReport().toString());
						}
					}
					payInfo.append("余额支付成功！");
					//生成申请记录
					applyColumn(columnOrder.getOrderNo(), null, 3);
				}else{
						throw new ServiceException("支付类型错误！");
				}
				return payInfo.toString();
			}
		}
	}


	@Override
	public boolean applyColumn(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException{
		try{
			//获取订单
			ColumnOrder columnOrder = columnOrderMapper.selectByOrderNo(orderNo);
			if(columnOrder == null){
				throw new ServiceException("订单不存在！");
			}
			//专栏申请
			ForumColumnApply forumColumnApply = new ForumColumnApply();
			forumColumnApply.setUserId(columnOrder.getUserId());
			forumColumnApply.setTypeId(columnOrder.getTypeId());
			forumColumnApply.setColumnName(columnOrder.getName());
			forumColumnApply.setStatus(0);//状态 0申请中 1通过 2未通过
			forumColumnApply.setAddTime(DateStampUtils.getTimesteamp());
			forumColumnApplyMapper.insert(forumColumnApply);
			//修改订单状态
			columnOrder.setStatus(2);
			columnOrderMapper.updateByPrimaryKey(columnOrder);
			//系统消息：您的【专栏名称】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知
			schedualMessageService.easemobMessage(columnOrder.getUserId().toString(),
					"您的【"+columnOrder.getName()+"】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知","1","1","");
			//余额账单
			schedualWalletService.addUserBalanceDetail(columnOrder.getUserId(),columnOrder.getAmount(),payType,2,orderNo,columnOrder.getName(),null,columnOrder.getUserId(),5);
			return true;
		}catch (Exception e){
			throw e;
		}
	}

	@Override
	public void handle(Integer userId, Integer applyId, Integer status,String coverImgUrl,String reason) throws ServiceException {
		ForumColumnApply forumColumnApply = forumColumnApplyMapper.selectByPrimaryKey(applyId);
		if(forumColumnApply == null){
			throw new ServiceException("申请信息不存在！");
		}else{
			if(forumColumnApply.getStatus() != 0){
				throw new ServiceException("申请信息状态异常！");
			}else{
				if(status == 1){
					if(StringUtils.isEmpty(coverImgUrl)){
						throw new ServiceException("封面图片地址不能为空");
					}
					ForumColumn forumColumn = new ForumColumn();
					forumColumn.setUserId(forumColumnApply.getUserId());
					forumColumn.setName(forumColumnApply.getColumnName());
					forumColumn.setCoverImgUrl(coverImgUrl);
					forumColumn.setFansCount(0);
					forumColumn.setAddTime(DateStampUtils.getTimesteamp());
					forumColumn.setIsChosen(2);//是否精选1是 2否
					forumColumn.setTypeId(forumColumnApply.getTypeId());
					forumColumnMapper.insert(forumColumn);
					//系统消息：您的【专栏名称】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知
					schedualMessageService.easemobMessage(forumColumnApply.getUserId().toString(),
							"恭喜您，您申请的专栏已通过官方审核！快拉您的好友一起来交流学习吧~","6","1",forumColumn.getId().toString());
				}else if(status == 2){
					if(StringUtils.isEmpty(reason)){
						throw new ServiceException("拒绝原因不能为空");
					}
					forumColumnApply.setReason(reason);
					//很抱歉您的【专栏名称】专栏审核未通过，可联系客服咨询详情
					schedualMessageService.easemobMessage(forumColumnApply.getUserId().toString(),
							"很抱歉您的【专栏名称】专栏审核未通过，可联系客服咨询详情","1","1","");
				}else{
					throw new ServiceException("状态值错误！");
				}
				forumColumnApply.setStatus(status);
				forumColumnApplyMapper.updateByPrimaryKeySelective(forumColumnApply);
			}

		}
	}

	@Override
	public List<ForumColumnApply> applyList(Integer start, Integer limit, String keyword,Integer status) throws ServiceException {
		//TODO 专栏申请列表
		List<ForumColumnApply> forumColumnApplies = forumColumnApplyMapper.selectApplyList(start * limit, limit, keyword,status);
		return forumColumnApplies;
	}

	@Override
	public MyColumnDto myColumn(Integer userId,Integer start,Integer limit) throws ServiceException {
		//获取专栏
		ForumColumn forumColumn = forumColumnMapper.selectByUserId(userId);
		if(forumColumn == null){
			throw new ServiceException("未找到专栏！");
		}
		MyColumnDto myColumnDto = new MyColumnDto();
		List<ForumInfoDto> forumList = forumInfoService.getForumList(userId,forumColumn.getId(), null, 1, null, start, limit, 1, null);
		Date date = DateUtil.getTimestamp(DateUtil.getCurrentTime(),DateUtil.DATE_FORMT_YEAR);
		Integer countByColumnId = forumPvMapper.getCountByColumnId(forumColumn.getId(), date);
		myColumnDto.setColumnId(forumColumn.getId());
		myColumnDto.setName(forumColumn.getName());
		myColumnDto.setTodayPvCount(countByColumnId);
		myColumnDto.setForumList(forumList);
		return myColumnDto;
	}

}
