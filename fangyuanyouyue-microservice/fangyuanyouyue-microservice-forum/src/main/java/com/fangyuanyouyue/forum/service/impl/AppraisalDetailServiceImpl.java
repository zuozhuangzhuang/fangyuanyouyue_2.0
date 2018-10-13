package com.fangyuanyouyue.forum.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.codingapi.tx.annotation.TxTransaction;
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
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.AppraisalCommentMapper;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.AppraisalImgMapper;
import com.fangyuanyouyue.forum.dao.AppraisalLikesMapper;
import com.fangyuanyouyue.forum.dao.AppraisalPvMapper;
import com.fangyuanyouyue.forum.dao.ArgueOrderMapper;
import com.fangyuanyouyue.forum.dao.CollectMapper;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.dto.AppraisalImgDto;
import com.fangyuanyouyue.forum.dto.admin.AdminAppraisalDetailDto;
import com.fangyuanyouyue.forum.model.AppraisalComment;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.AppraisalImg;
import com.fangyuanyouyue.forum.model.AppraisalLikes;
import com.fangyuanyouyue.forum.model.ArgueOrder;
import com.fangyuanyouyue.forum.model.Collect;
import com.fangyuanyouyue.forum.model.UserInfo;
import com.fangyuanyouyue.forum.param.AdminForumParam;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;


@Service(value = "appraisalDetailService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalDetailServiceImpl implements AppraisalDetailService {

	@Autowired
	private AppraisalDetailMapper appraisalDetailMapper;
	@Autowired
	private AppraisalImgMapper appraisalImgMapper;
	@Autowired
	private AppraisalCommentServiceImpl appraisalCommentServiceImpl;
	@Autowired
	private AppraisalLikesServiceImpl appraisalLikesServiceImpl;
	@Autowired
	private AppraisalPvServiceImpl appraisalPvServiceImpl;
	@Autowired
	private CollectMapper collectMapper;
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private AppraisalLikesMapper appraisalLikesMapper;
	@Autowired
	AppraisalCommentMapper appraisalCommentMapper;
	@Autowired
	private SchedualMessageService schedualMessageService;
	@Autowired
	private SchedualWalletService schedualWalletService;
	@Autowired
	private ArgueOrderMapper argueOrderMapper;
	@Autowired
	private AppraisalPvMapper appraisalPvMapper;


	@Override
	public AppraisalDetailDto getAppraisalDetail(Integer userId,Integer appraisalId) throws ServiceException {
		AppraisalDetail model = appraisalDetailMapper.selectDetailByPrimaryKey(appraisalId);
		if(model == null || model.getStatus().equals(Status.DELETE.getValue())){
			throw new ServiceException("未找到全民鉴定！");
		}
		AppraisalDetailDto dto = new AppraisalDetailDto(model);
		//评论数量
		Integer commentCount = appraisalCommentServiceImpl.countComment(appraisalId);
		dto.setCommentCount(commentCount);
		//点赞数量
		Integer likesCount = appraisalLikesServiceImpl.countLikes(appraisalId);
		dto.setLikesCount(likesCount);
		//浏览量
		Integer viewCount = appraisalPvServiceImpl.countPv(appraisalId);
		dto.setViewCount(viewCount+model.getPvCount());
		//看真数量
		Integer truthCount = appraisalCommentServiceImpl.countComment(appraisalId,StatusEnum.YES.getValue());
		//看假数量
		Integer untruthCount = appraisalCommentServiceImpl.countComment(appraisalId,StatusEnum.NO.getValue());
		dto.setTruthCount(truthCount);
		dto.setUntruthCount(untruthCount);

		if(userId!=null) {
			//增加浏览量
			appraisalPvServiceImpl.savePv(userId,appraisalId);
			//是否收藏
			Collect collect = collectMapper.selectByCollectIdType(userId, appraisalId, 5);
			if(collect != null){
				dto.setIsCollect(StatusEnum.YES.getValue());
			}
//			//是否关注作者
//			boolean isFans = JSONObject.parseObject(schedualUserService.isFans(userId,model.getUserId())).getBoolean("data");
//			if(isFans){
//				dto.setIsFans(StatusEnum.YES.getValue());
//			}
			//是否点赞
			AppraisalLikes appraisalLikes = appraisalLikesMapper.selectByAppraisalIdUserId(appraisalId, userId);
			if(appraisalLikes != null){
				dto.setIsLikes(StatusEnum.YES.getValue());
			}
			//浏览量
			Integer pvCount = appraisalPvServiceImpl.countPv(appraisalId);
			dto.setViewCount(pvCount+model.getPvCount());
			AppraisalComment comment = appraisalCommentMapper.selectByAppraisalIdUserId(userId, appraisalId);
			if(comment != null){
				//用户观点
				dto.setViewpoint(comment.getViewpoint());
				//是否获胜
				dto.setIsWinner(comment.getIsWinner());
			}
		}
		//鉴定图片列表
		List<AppraisalImg> appraisalImgs = appraisalImgMapper.selectListByAppraisal(appraisalId);
		dto.setImgDtos(AppraisalImgDto.toDtoList(appraisalImgs));
		return dto;
	}

	@Override
	public List<AppraisalDetailDto> getAppraisalList(Integer userId,String keyword,Integer start, Integer limit,Integer type) throws ServiceException {
		List<AppraisalDetail> list;
		if(type == null){
			list = appraisalDetailMapper.selectMyList(null, keyword, start*limit, limit);
		}else{
			if(userId == null){
				throw new ServiceException("用户id不能为空！");
			}
			if(type == 1){
				list = appraisalDetailMapper.selectListWithMe(userId, keyword, start*limit, limit);
			}else if(type == 2){
				//userId 不为空：我的全民鉴定列表
				list = appraisalDetailMapper.selectMyList(userId, keyword, start*limit, limit);
			}else{
				throw new ServiceException("类型错误！");
			}
		}
		List<AppraisalDetailDto> dtos = new ArrayList<>();
		//列表不需要返回点赞数、浏览量、评论量
		for(AppraisalDetail model:list) {
			AppraisalDetailDto dto = new AppraisalDetailDto(model);
			if(userId != null){
				//是否收藏
				Collect collect = collectMapper.selectByCollectIdType(userId, model.getId(), 5);
				if(collect != null){
					dto.setIsCollect(StatusEnum.YES.getValue());
				}
			}
			//参与鉴定用户头像列表
			List<String> headImgUrls = new ArrayList<>();
			List<AppraisalComment> appraisalComments = appraisalCommentMapper.selectByAppraisalId(model.getId(), 0, 5);
			if(appraisalComments != null && appraisalComments.size() > 0){
				for(AppraisalComment comment:appraisalComments){
					headImgUrls.add(comment.getHeadImgUrl());
				}
			}
			dto.setHeadImgUrls(headImgUrls);
			//鉴定图片列表
			List<AppraisalImg> appraisalImgs = appraisalImgMapper.selectListByAppraisal(model.getId());
			dto.setImgDtos(AppraisalImgDto.toDtoList(appraisalImgs));
			AppraisalComment comment = appraisalCommentMapper.selectByAppraisalIdUserId(userId,model.getId());
			if(comment != null){
				//用户观点
				dto.setViewpoint(comment.getViewpoint());
				//是否获胜
				dto.setIsWinner(comment.getIsWinner());
			}
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional
	@TxTransaction(isStart=true)
	public Object addAppraisal(Integer userId, BigDecimal bonus, String title, String content,String[] imgUrls,Integer[] userIds,Integer payType,String payPwd) throws ServiceException {

		StringBuffer payInfo = new StringBuffer();
		if(bonus != null && bonus.compareTo(new BigDecimal(0)) > 0){
			//生成订单
			ArgueOrder argueOrder = new ArgueOrder();
			argueOrder.setUserId(userId);
			//订单号
			final IdGenerator idg = IdGenerator.INSTANCE;
			String id = idg.nextId();
			argueOrder.setOrderNo(id);
			argueOrder.setAmount(bonus);
			argueOrder.setStatus(Status.ORDER_UNPAID.getValue());
			argueOrder.setAddTime(DateStampUtils.getTimesteamp());
			argueOrder.setTitle(title);
			argueOrder.setContent(content);
			//保存邀请好友id到订单
			StringBuffer toUsers = new StringBuffer();
			if(userIds != null && userIds.length > 0){
				for(Integer toUser:userIds){
					if(toUser.intValue() == userIds[userIds.length-1].intValue()){
						toUsers.append(toUser);
					}else{
						toUsers.append(toUser+",");
					}
				}
			}
			//保存图片路径到订单
			StringBuffer imgs = new StringBuffer();
			for(String imgUrl:imgUrls){
				if(imgUrl.equals(imgUrls[imgUrls.length-1])){
					imgs.append(imgUrl);
				}else{
					imgs.append(imgUrl+",");
				}
			}
			argueOrder.setImgUrls(imgs.toString());
			argueOrder.setToUsers(toUsers.toString());
			argueOrderMapper.insert(argueOrder);

			//支付
			if(payType.intValue() == Status.PAY_TYPE_WECHAT.getValue()){
				String getWechatOrder = schedualWalletService.orderPayByWechat(argueOrder.getOrderNo(), argueOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.argue_wechat_notify.getNotifUrl());
				BaseResp result = ParseReturnValue.getParseReturnValue(getWechatOrder);
				if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(result.getCode(),result.getReport());
				}
				WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
				return wechatPayDto;
			}else if(payType.intValue() == Status.PAY_TYPE_ALIPAY.getValue()){
				String getALiOrder = schedualWalletService.orderPayByALi(argueOrder.getOrderNo(), argueOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.argue_alipay_notify.getNotifUrl());
				BaseResp result = ParseReturnValue.getParseReturnValue(getALiOrder);
				if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(result.getCode(),result.getReport());
				}
				payInfo.append(result.getData());
			}else if(payType.intValue() == Status.PAY_TYPE_BALANCE.getValue()){
				String verifyPayPwd = schedualUserService.verifyPayPwd(userId, payPwd);
				BaseResp result = ParseReturnValue.getParseReturnValue(verifyPayPwd);
				if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(result.getCode(),result.getReport());
				}
				if (!(boolean)result.getData()) {
					throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
				}else{
					BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(userId, bonus, Status.SUB.getValue()));
					if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
						throw new ServiceException(baseResp.getCode(),baseResp.getReport());
					}
				}
				payInfo.append("余额支付成功！");
				//生成全民鉴定信息
				applyAppraisal(argueOrder.getOrderNo(),null,Status.PAY_TYPE_BALANCE.getValue());
			}else if(payType.intValue() == Status.PAY_TYPE_MINI.getValue()){
				String getMiniOrder = schedualWalletService.orderPayByWechatMini(userId,argueOrder.getOrderNo(), argueOrder.getAmount(), NotifyUrl.mini_notify.getNotifUrl()+NotifyUrl.argue_wechat_notify.getNotifUrl());
				BaseResp result = ParseReturnValue.getParseReturnValue(getMiniOrder);
				if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(result.getCode(),result.getReport());
				}
				WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
				return wechatPayDto;
			}else{
				throw new ServiceException("支付方式错误！");
			}
		}else{

			AppraisalDetail appraisalDetail = new AppraisalDetail();
			appraisalDetail.setUserId(userId);
			appraisalDetail.setTitle(title);
			if(StringUtils.isNotEmpty(content)){
				appraisalDetail.setContent(content);
			}
			appraisalDetail.setStatus(StatusEnum.UNDERWAY.getValue());
			//结束时间为7天后
			appraisalDetail.setEndTime(DateUtil.getDateAfterDay(DateStampUtils.getTimesteamp(),7));
			appraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
			appraisalDetail.setPvCount(0);
			appraisalDetail.setBonus(new BigDecimal(0));
			appraisalDetail.setCommentTime(DateStampUtils.getTimesteamp());
			appraisalDetailMapper.insert(appraisalDetail);
			//存储图片
			insertAppraisalImg(imgUrls, appraisalDetail);
			//邀请我：用户“用户昵称”发起全民鉴定【全名鉴定名称】时邀请了您！点击此处前往查看吧
			String verifyUserById = schedualUserService.verifyUserById(userId);
			BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(verifyUserById);
			if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
				throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
			}
			UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
			if(userIds != null && userIds.length > 0){
				for(Integer toUserId:userIds){
					schedualMessageService.easemobMessage(toUserId.toString(),
							"用户“"+user.getNickName()+"”发起全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了您！点击此处前往查看吧",Status.INVITE_MESSAGE.getMessage(),Status.JUMP_TYPE_APPRAISAL.getMessage(),appraisalDetail.getId().toString());
				}
			}
		}
		return payInfo.toString();
	}

	/**
	 * 新增鉴定图片
	 * @param imgUrls
	 * @param appraisalDetail
	 */
	private void insertAppraisalImg(String[] imgUrls, AppraisalDetail appraisalDetail) {
		for(String imgUrl:imgUrls){
			//存储图片
			AppraisalImg appraisalImg = new AppraisalImg();
			appraisalImg.setImgUrl(imgUrl);
			appraisalImg.setAppraisalId(appraisalDetail.getId());
			appraisalImg.setAddTime(DateStampUtils.getTimesteamp());
			appraisalImgMapper.insert(appraisalImg);
		}
	}

	@Override
	@Transactional
	@TxTransaction(isStart=true)
	public boolean applyAppraisal(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException{
		//获取订单
		ArgueOrder argueOrder = argueOrderMapper.selectByOrderNo(orderNo);
		if(argueOrder == null){
			throw new ServiceException("订单不存在！");
		}
		//生成全民鉴定
		AppraisalDetail appraisalDetail = new AppraisalDetail();
		appraisalDetail.setBonus(argueOrder.getAmount());
		appraisalDetail.setUserId(argueOrder.getUserId());
		appraisalDetail.setTitle(argueOrder.getTitle());
		appraisalDetail.setContent(argueOrder.getContent());
		appraisalDetail.setStatus(StatusEnum.UNDERWAY.getValue());//状态 1进行中 2结束
		//结束时间为7天后
		appraisalDetail.setEndTime(DateUtil.getDateAfterDay(DateStampUtils.getTimesteamp(),7));
		appraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
		appraisalDetail.setPvCount(0);
		appraisalDetail.setCommentTime(DateStampUtils.getTimesteamp());
		appraisalDetailMapper.insert(appraisalDetail);
		//存储图片
		String[] imgUrls = argueOrder.getImgUrls().split(",");
		insertAppraisalImg(imgUrls, appraisalDetail);
		String[] userInds = argueOrder.getToUsers().split(",");
		//邀请我：用户“用户昵称”发起全民鉴定【全名鉴定名称】时邀请了您！点击此处前往查看吧
		String verifyUserById = schedualUserService.verifyUserById(argueOrder.getUserId());
		BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(verifyUserById);
		if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
			throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
		}
		UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
		if(userInds != null && userInds.length > 0){
			for(String toUserId:userInds){
				schedualMessageService.easemobMessage(toUserId,
						"用户“"+user.getNickName()+"”发起全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了您！点击此处前往查看吧",Status.INVITE_MESSAGE.getMessage(),Status.JUMP_TYPE_APPRAISAL.getMessage(),appraisalDetail.getId().toString());
			}
		}
		//余额账单
        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(argueOrder.getUserId(),argueOrder.getAmount(),payType,Status.EXPEND.getValue(),orderNo,"【"+argueOrder.getTitle()+"】",argueOrder.getUserId(),null, Status.APPRAISAL.getValue(),thirdOrderNo));
        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
        }
		argueOrder.setStatus(Status.ORDER_COMPLETE.getValue());
		argueOrder.setPayNo(thirdOrderNo);
		argueOrderMapper.updateByPrimaryKey(argueOrder);
		return true;
	}

	@Override
	public void invite(Integer userId,Integer appraisalId, Integer[] userIds) throws ServiceException {
		AppraisalDetail appraisalDetail = appraisalDetailMapper.selectByPrimaryKey(appraisalId);
		if(appraisalDetail == null || appraisalDetail.getStatus().equals(Status.DELETE.getValue())){
			throw new ServiceException("未找到全民鉴定状态！");
		}
		//邀请我：用户“用户昵称”发起全民鉴定【全名鉴定名称】时邀请了您！点击此处前往查看吧
		String verifyUserById = schedualUserService.verifyUserById(userId);
		BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(verifyUserById);
		if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
			throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
		}
		UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
		if(userIds != null && userIds.length > 0){
			for(Integer toUserId:userIds){
				schedualMessageService.easemobMessage(toUserId.toString(),
						"用户“"+user.getNickName()+"”看到全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了您！点击此处前往查看吧",Status.INVITE_MESSAGE.getMessage(),Status.JUMP_TYPE_APPRAISAL.getMessage(),appraisalDetail.getId().toString());
			}
		}
	}


	@Override
	public Pager getPage(BasePageReq param) {

		Integer total = appraisalDetailMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
		List<AppraisalDetail> datas = appraisalDetailMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
		//计算浏览量
		List<AdminAppraisalDetailDto> dtos = new ArrayList<AdminAppraisalDetailDto>();
		for(AppraisalDetail model:datas) {
			AdminAppraisalDetailDto dto = new AdminAppraisalDetailDto(model);
			//鉴定图片列表
			List<AppraisalImg> appraisalImgs = appraisalImgMapper.selectListByAppraisal(model.getId());
			dto.setImgDtos(AppraisalImgDto.toDtoList(appraisalImgs));
			
			Integer count = appraisalPvMapper.countById(model.getId());

			dto.setRealCount(count);
			
			dto.setTotalCount(dto.getBaseCount()+dto.getRealCount());
			dtos.add(dto);
		}
		
		Pager pager = new Pager();
		pager.setTotal(total);
		pager.setDatas(dtos);
		return pager;
	}

	@Override
	public void deleteAppraisal(Integer userId, Integer[] ids) throws ServiceException {
		for(Integer id:ids){
			AppraisalDetail detail = appraisalDetailMapper.selectByPrimaryKey(id);
			if(detail == null || detail.getStatus().equals(Status.DELETE.getValue())){
				throw new ServiceException("未找到全民鉴定！");
			}else{
				if(!detail.getStatus().equals(Status.END.getValue())){
					throw new ServiceException("全民鉴定未结束！");
				}else{
					if(detail.getUserId().equals(userId)){
						detail.setStatus(Status.DELETE.getValue());
						appraisalDetailMapper.updateByPrimaryKey(detail);
					}else{
						throw new ServiceException("无权删除！");
					}
				}
			}
		}
	}

    @Override
	@Transactional
	@TxTransaction(isStart=true)
    public void updateAppraisal(AdminForumParam param) throws ServiceException {
        AppraisalDetail detail = appraisalDetailMapper.selectByPrimaryKey(param.getId());
        if(detail == null){
            throw new ServiceException("未找到全民鉴定");
        }
        if(param.getSort() != null){
            detail.setSort(param.getSort());
        }
        if(param.getStatus() != null){
            detail.setStatus(param.getStatus());
        }
        if(param.getCount() != null){
            detail.setPvCount(param.getCount());
        }
        appraisalDetailMapper.updateByPrimaryKey(detail);
        if(param.getStatus() != null && param.getStatus().equals(Status.DELETE.getValue())){
            if(StringUtils.isEmpty(param.getContent())){
                throw new ServiceException("删除理由不能为空！");
            }
			if(detail.getBonus() != null){
				BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(detail.getUserId(),detail.getBonus(),Status.ADD.getValue()));
				if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(baseResp.getCode(),baseResp.getReport());
				}
				//余额账单
				//订单号
				final IdGenerator idg = IdGenerator.INSTANCE;
				String orderNo = idg.nextId();
				baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(detail.getUserId(),detail.getBonus(), Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),orderNo,"【"+detail.getTitle()+"】官方删除",detail.getUserId(),null,Status.APPRAISAL.getValue(),orderNo));
				if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(baseResp.getCode(),baseResp.getReport());
				}
			}
            //很抱歉，您的帖子/视频/全民鉴定/【名称】已被官方删除，删除理由：……
            schedualMessageService.easemobMessage(detail.getUserId().toString(),
                    "很抱歉，您的全民鉴定【"+detail.getTitle()+"】已被官方删除，删除理由："+param.getContent(), Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
        }
    }
}
