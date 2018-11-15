package com.fangyuanyouyue.forum.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.enums.MiniMsg;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.forum.dao.ColumnOrderMapper;
import com.fangyuanyouyue.forum.dao.ForumColumnApplyMapper;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dao.ForumColumnTypeMapper;
import com.fangyuanyouyue.forum.dao.ForumPvMapper;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.dto.MyColumnDto;
import com.fangyuanyouyue.forum.dto.admin.AdminForumColumnApplyDto;
import com.fangyuanyouyue.forum.dto.admin.AdminForumColumnDto;
import com.fangyuanyouyue.forum.model.ColumnOrder;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.model.ForumColumnApply;
import com.fangyuanyouyue.forum.model.ForumColumnType;
import com.fangyuanyouyue.forum.model.UserInfo;
import com.fangyuanyouyue.forum.service.ForumColumnService;
import com.fangyuanyouyue.forum.service.ForumInfoService;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;


@Service(value = "forumColumnService")
@Transactional(rollbackFor=Exception.class)
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
	public List<ForumColumnTypeDto> getColumnList(Integer typeId,Integer start, Integer limit) throws ServiceException {
		List<ForumColumnTypeDto> forumTypeList;
		if(typeId == null){
			forumTypeList = getForumTypeList();
			for(ForumColumnTypeDto dto:forumTypeList){
				List<ForumColumn> list = forumColumnMapper.selectPage(dto.getTypeId(),start*limit, limit);
				List<ForumColumnDto> dtos = ForumColumnDto.toDtoList(list);
				dto.setForumColumnList(dtos);
			}
		}else{
			List<ForumColumn> list = forumColumnMapper.selectPage(typeId,start*limit, limit);
			List<ForumColumnDto> dtos = ForumColumnDto.toDtoList(list);
			forumTypeList = ForumColumnTypeDto.toDtoList(dtos);
		}
		return forumTypeList;
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
	@Transactional(rollbackFor=Exception.class)
	@TxTransaction(isStart=true)
	public Object addColumn(Integer userId, Integer typeId,String name,Integer payType,String payPwd) throws ServiceException {
		String verifyUserById = schedualUserService.verifyUserById(userId);
		BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(verifyUserById);
		if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
			throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
		}
		UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
		//验证手机号
		if(StringUtils.isEmpty(user.getPhone())){
			throw new ServiceException(ReCode.NO_PHONE.getValue(),ReCode.NO_PHONE.getMessage());
		}
		//name已存在的申请时返回
		if(forumColumnMapper.selectByName(name)!=null){
			throw new ServiceException("专栏名已存在！");
		}
		ForumColumnType forumColumnType = forumColumnTypeMapper.selectByPrimaryKey(typeId);
		if(forumColumnType == null){
			throw new ServiceException("分类异常！");
		}else{
			ForumColumnApply forumColumnApply = forumColumnApplyMapper.selectApplyByUserId(userId);
			if(forumColumnApply != null){
				throw new ServiceException("您已申请专栏【"+forumColumnApply.getColumnName()+"】，正在审核中，请耐心等待。");
			}
			ForumColumn forumColumn = forumColumnMapper.selectByUserId(userId);
			if(forumColumn != null){
				throw new ServiceException("每位用户只能申请一个专栏！");
			}else{
				ColumnOrder columnOrder = new ColumnOrder();
				columnOrder.setAmount(new BigDecimal(200));
				columnOrder.setUserId(userId);
				columnOrder.setStatus(Status.ORDER_UNPAID.getValue());//状态 1待支付 2已完成 3已删除
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
				if(payType.intValue() == Status.PAY_TYPE_WECHAT.getValue()){
					String getWechatOrder = schedualWalletService.orderPayByWechat(columnOrder.getOrderNo(), columnOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.column_wechat_notify.getNotifUrl());
					BaseResp result = ParseReturnValue.getParseReturnValue(getWechatOrder);
					if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(result.getCode(),result.getReport());
					}
					WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
					return wechatPayDto;
				}else if(payType.intValue() == Status.PAY_TYPE_ALIPAY.getValue()){
					String getALiOrder = schedualWalletService.orderPayByALi(columnOrder.getOrderNo(), columnOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.column_alipay_notify.getNotifUrl());
					BaseResp result = ParseReturnValue.getParseReturnValue(getALiOrder);
					if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(result.getCode(),result.getReport());
					}
					payInfo.append(result.getData());
				}else if(payType.intValue() == Status.PAY_TYPE_BALANCE.getValue()) {
					String verifyPayPwd = schedualUserService.verifyPayPwd(userId, payPwd);
					BaseResp result = ParseReturnValue.getParseReturnValue(verifyPayPwd);
					if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(result.getCode(),result.getReport());
					}
					if (!(boolean)result.getData()) {
						throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
					}else {
						//调用wallet-service修改余额功能,成为栏主200元/人
						BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(userId, new BigDecimal(200), Status.SUB.getValue()));
						if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
							throw new ServiceException(baseResp.getCode(),baseResp.getReport());
						}
					}
					payInfo.append("余额支付成功！");
					//生成申请记录
					applyColumn(columnOrder.getOrderNo(), null, Status.PAY_TYPE_BALANCE.getValue());
				}else if(payType.intValue() == Status.PAY_TYPE_MINI.getValue()){
					String getMiniOrder = schedualWalletService.orderPayByWechatMini(userId,columnOrder.getOrderNo(), columnOrder.getAmount(), NotifyUrl.mini_notify.getNotifUrl()+NotifyUrl.column_wechat_notify.getNotifUrl());
					BaseResp result = ParseReturnValue.getParseReturnValue(getMiniOrder);
					if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(result.getCode(),result.getReport());
					}
					WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
					return wechatPayDto;
				}else{
					throw new ServiceException("支付方式错误！");
				}
				return payInfo.toString();
			}
		}
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	@TxTransaction(isStart=true)
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
			columnOrder.setStatus(Status.ORDER_COMPLETE.getValue());
			columnOrder.setPayNo(thirdOrderNo);
			columnOrderMapper.updateByPrimaryKey(columnOrder);
			//订单号
			final IdGenerator idg = IdGenerator.INSTANCE;
			String id = idg.nextId();
			//系统消息：您的【专栏名称】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知
			schedualMessageService.easemobMessage(columnOrder.getUserId().toString(),
					"您的【"+columnOrder.getName()+"】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
			//余额账单
            BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(columnOrder.getUserId(),columnOrder.getAmount(),payType,Status.EXPEND.getValue(),orderNo,"申请专栏【"+columnOrder.getName()+"】",null,columnOrder.getUserId(),Status.FORUM_COLUMN.getValue(),thirdOrderNo));
            if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(baseResp.getCode(),baseResp.getReport());
            }
			return true;
		}catch (Exception e){
			throw e;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@TxTransaction(isStart=true)
	public void handle(Integer applyId, Integer status,String coverImgUrl,String reason) throws ServiceException {
		ForumColumnApply forumColumnApply = forumColumnApplyMapper.selectByPrimaryKey(applyId);
		if(forumColumnApply == null){
			throw new ServiceException("申请信息不存在！");
		}else{
			if(forumColumnApply.getStatus() != 0){
				throw new ServiceException("申请信息状态异常！");
			}else {
				if (status.intValue() == Status.YES.getValue()) {
					if (StringUtils.isEmpty(coverImgUrl)) {
						throw new ServiceException("封面图片地址不能为空");
					}
					ForumColumn forumColumn = new ForumColumn();
					forumColumn.setUserId(forumColumnApply.getUserId());
					forumColumn.setName(forumColumnApply.getColumnName());
					forumColumn.setCoverImgUrl(coverImgUrl);
					forumColumn.setFansCount(0);
					forumColumn.setAddTime(DateStampUtils.getTimesteamp());
					forumColumn.setIsChosen(Status.NO.getValue());//是否精选1是 2否
					forumColumn.setTypeId(forumColumnApply.getTypeId());
					forumColumnMapper.insert(forumColumn);
					//系统消息：您的【专栏名称】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知
					schedualMessageService.easemobMessage(forumColumnApply.getUserId().toString(),
							"恭喜您，您申请的专栏已通过官方审核！快拉您的好友一起来交流学习吧~",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_COLUMN_AGREE.getMessage(),forumColumn.getId().toString());
				}else if(status.intValue() == Status.NO.getValue()){
					if(StringUtils.isEmpty(reason)){
						reason = "官方审核未通过";
					}
					forumColumnApply.setReason(reason);
					//很抱歉您的【专栏名称】专栏审核未通过，可联系客服咨询详情
					schedualMessageService.easemobMessage(forumColumnApply.getUserId().toString(),
							"很抱歉您的【"+forumColumnApply.getColumnName()+"】专栏审核未通过，原因："+reason+"。可联系客服咨询详情",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_COLUMN_REFUSE.getMessage(),"");
					//余额账单
					BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(forumColumnApply.getUserId(),new BigDecimal(200),Status.ADD.getValue()));
					if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(baseResp.getCode(),baseResp.getReport());
					}
					//订单号
					final IdGenerator idg = IdGenerator.INSTANCE;
					String orderNo = idg.nextId();
					baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(forumColumnApply.getUserId(),new BigDecimal(200), Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),orderNo,"【"+forumColumnApply.getColumnName()+"】审核未通过",null,forumColumnApply.getUserId(),Status.FORUM_COLUMN.getValue(),orderNo));
					if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(baseResp.getCode(),baseResp.getReport());
					}

				}else{
					throw new ServiceException("状态值错误！");
				}
				//TODO 发送微信消息
				//openId
				BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getFormId(forumColumnApply.getUserId()));
				if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(baseResp.getCode(),baseResp.getReport());
				}
				if(baseResp.getData() != null){
                    String formId = baseResp.getData().toString();

                    //formId
                    baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getOpenId(forumColumnApply.getUserId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    String openId = baseResp.getData().toString();

                    UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), UserInfo.class);
                    Map<String,Object> map = new HashMap<>();
					//名称
					map.put("keyword1",user.getNickName());
					//审核类别
					map.put("keyword2",forumColumnApply.getColumnName());
					//审核结果
					map.put("keyword3",status.equals(Status.YES.getValue())?"您的专栏申请已通过":"您的专栏申请未通过");
					//备注
					map.put("keyword4",status.equals(Status.YES.getValue())?"无":reason);
                    schedualMessageService.wechatMessage(openId, MiniMsg.SYSTEM_MSG.getTemplateId(),MiniMsg.SYSTEM_MSG.getPagePath(),map,formId);
                }
				forumColumnApply.setStatus(status);
				forumColumnApplyMapper.updateByPrimaryKeySelective(forumColumnApply);
			}

		}
	}

	@Override
	public List<ForumColumnApply> applyList(Integer start, Integer limit, String keyword,Integer status) throws ServiceException {
		//专栏申请列表
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
		Integer countByColumnId = forumPvMapper.getCountByColumnId(forumColumn.getId(), date,null,1);
		myColumnDto.setColumnId(forumColumn.getId());
		myColumnDto.setName(forumColumn.getName());
		myColumnDto.setTodayPvCount(countByColumnId);
		myColumnDto.setForumList(forumList);
		return myColumnDto;
	}


	@Override
	public Pager getPage(BasePageReq param) {

		Integer total = forumColumnMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
		List<ForumColumn> datas = forumColumnMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
		ArrayList<AdminForumColumnDto> dtos = new ArrayList<AdminForumColumnDto>();
		for(ForumColumn model:datas) {
			AdminForumColumnDto dto = new AdminForumColumnDto(model);
			
			Integer count =forumPvMapper.getCountByColumnId(model.getId(), null, null, null);
			dto.setTotalCount(count);
			
			dtos.add(dto);
		}
		Pager pager = new Pager();
		pager.setTotal(total);
		pager.setDatas(dtos);
		return pager;
	}



	@Override
	public Pager getPageApply(BasePageReq param) {

		Integer total = forumColumnApplyMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
		List<ForumColumnApply> datas = forumColumnApplyMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
		Pager pager = new Pager();
		pager.setTotal(total);
		pager.setDatas(AdminForumColumnApplyDto.toDtoList(datas));
		return pager;
	}

    @Override
    public void updateColumn(Integer columnId, String name, String coverImgUrl, Integer isChosen) throws ServiceException {
        ForumColumn forumColumn = forumColumnMapper.selectByPrimaryKey(columnId);
        if(forumColumn == null){
            throw new ServiceException("未找到专栏！");
        }
        if(StringUtils.isNotEmpty(name)){
            forumColumn.setName(name);
        }
        if(StringUtils.isNotEmpty(coverImgUrl)){
            forumColumn.setCoverImgUrl(coverImgUrl);
        }
        forumColumnMapper.updateByPrimaryKey(forumColumn);
    }

	@Override
	public Integer isHasColumn(Integer userId) throws ServiceException {
		ForumColumn forumColumn = forumColumnMapper.selectByUserId(userId);
		ForumColumnApply forumColumnApply = forumColumnApplyMapper.selectApplyByUserId(userId);
		if(forumColumn != null){
			return 1;
		}else if(forumColumnApply != null){
			return 3;
		}else{
			return 2;
		}
	}
}
