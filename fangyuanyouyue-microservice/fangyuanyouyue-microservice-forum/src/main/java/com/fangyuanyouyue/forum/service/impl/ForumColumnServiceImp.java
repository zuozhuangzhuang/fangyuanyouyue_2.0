package com.fangyuanyouyue.forum.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.dao.ForumColumnApplyMapper;
import com.fangyuanyouyue.forum.dao.ForumColumnTypeMapper;
import com.fangyuanyouyue.forum.model.ForumColumnApply;
import com.fangyuanyouyue.forum.model.ForumColumnType;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.service.ForumColumnService;


@Service(value = "forumColumnService")
public class ForumColumnServiceImp implements ForumColumnService {

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
	public void addColumn(Integer userId, Integer typeId,String name,Integer payType,String payPwd) throws ServiceException {
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
				StringBuffer payInfo = new StringBuffer();
				switch (payType){
					case 1://TODO 微信，回调失败不做操作，成功就在回调接口中继续申请栏主
						payInfo.append("微信支付回调！");
						break;
					case 2://TODO 支付宝，回调失败不做操作，成功就在回调接口中继续申请栏主
						payInfo.append("支付宝支付回调！");
						break;
					case 3://余额
						//验证支付密码
						Boolean verifyPayPwd = JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getBoolean("data");
						if(!verifyPayPwd){
							throw new ServiceException("支付密码错误！");
						}else{
							//调用wallet-service修改余额功能,成为栏主200元/人
							schedualWalletService.updateBalance(userId,new BigDecimal(200),2);
						}
						payInfo.append("余额支付成功！");

						ForumColumnApply forumColumnApply = new ForumColumnApply();
						forumColumnApply.setUserId(userId);
						forumColumnApply.setTypeId(typeId);
						forumColumnApply.setColumnName(name);
						forumColumnApply.setStatus(0);//状态 0申请中 1通过 2未通过
						forumColumnApply.setAddTime(DateStampUtils.getTimesteamp());
						forumColumnApplyMapper.insert(forumColumnApply);
						//在后台管理同意后添加到

						//系统消息：您的【专栏名称】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知
						schedualMessageService.easemobMessage(userId.toString(),
								"您的【"+name+"】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知","1","1","");
						break;
					default:
						throw new ServiceException("支付类型错误！");
				}
			}
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
	public List<ForumColumnApply> applyList(Integer start, Integer limit, String keyword) throws ServiceException {

		return null;
	}
}
