package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.*;
import com.fangyuanyouyue.forum.model.AppraisalComment;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.model.ForumPv;
import com.fangyuanyouyue.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "timerService")
@Transactional(rollbackFor=Exception.class)
public class TimerServiceImpl implements TimerService{

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
    private AppraisalCommentMapper appraisalCommentMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private AppraisalCommentLikesService appraisalCommentLikesService;
    @Autowired
    private AppraisalCommentLikesMapper appraisalCommentLikesMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;
    @Autowired
    private ForumPvMapper forumPvMapper;
    @Autowired
    private ForumColumnMapper forumColumnMapper;


    @Override
    public void appraisalEnd() throws ServiceException {
        //1、获取status=1，enndTime小于当前时间的全民鉴定 2、获取鉴定中评论获取点赞最多的评论，定为获胜者(票数一致最先评论者胜出) 3、向发起者和获胜者发送消息通知
        List<AppraisalDetail> details = appraisalDetailMapper.selectByStatusEndTime(StatusEnum.UNDERWAY.getValue(), DateStampUtils.getTimesteamp());
        if(details != null && details.size() > 0){
            for(AppraisalDetail detail:details){
                List<AppraisalComment> comments= appraisalCommentMapper.selectByAppraisalId(detail.getId(), null, null);
                if(comments == null || comments.size() < 1){
                    //无人评论，退回奖金
                    if(detail.getBonus() != null){
                        BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(detail.getUserId(),detail.getBonus(),Status.ADD.getValue())), BaseResp.class);
                        if(baseResp.getCode() == 1){
                            throw new ServiceException(baseResp.getReport().toString());
                        }
                        //余额账单
                        //订单号
                        final IdGenerator idg = IdGenerator.INSTANCE;
                        String id = idg.nextId();
                        schedualWalletService.addUserBalanceDetail(detail.getUserId(),detail.getBonus(), Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),id,detail.getTitle(),detail.getUserId(),null,Status.APPRAISAL.getValue());
                    }
                }else{
                    for(int i=0;i<comments.size();i++){
                        AppraisalComment comment = comments.get(i);
                        if(i==0){
                            //第一个胜出
                            comment.setIsWinner(StatusEnum.YES.getValue());
                            appraisalCommentMapper.updateByPrimaryKey(comment);
                            //胜者增加余额
                            if(detail.getBonus() != null){
                                BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(comment.getUserId(),detail.getBonus(),Status.ADD.getValue())), BaseResp.class);
                                if(baseResp.getCode() == 1){
                                    throw new ServiceException(baseResp.getReport().toString());
                                }
                            }
                            //恭喜您！您参与的全民鉴定【名称】，您获得了最高票数！点击查看最终结果吧~
                            schedualMessageService.easemobMessage(detail.getUserId().toString(),
                                    "恭喜您！您参与的全民鉴定【"+detail.getTitle()+"】，您获得了最高票数！点击查看最终结果吧~","7","1",detail.getId().toString());
                            //余额账单
                            //订单号
                            final IdGenerator idg = IdGenerator.INSTANCE;
                            String id = idg.nextId();
                            schedualWalletService.addUserBalanceDetail(comment.getUserId(),detail.getBonus(), Status.PAY_TYPE_BALANCE.getValue(),Status.INCOME.getValue(),id,detail.getTitle(),detail.getUserId(),comment.getUserId(),Status.APPRAISAL.getValue());
                        }else{
                            comment.setIsWinner(StatusEnum.NO.getValue());
                            appraisalCommentMapper.updateByPrimaryKey(comment);
                            //您参与的全民鉴定【名称】已结束投票，点击查看最终结果吧
                            schedualMessageService.easemobMessage(detail.getUserId().toString(),
                                    "您参与的全民鉴定【"+detail.getTitle()+"】已结束投票，点击查看最终结果吧","7","1",detail.getId().toString());
                        }
                    }
                }
                detail.setStatus(StatusEnum.END.getValue());
                appraisalDetailMapper.updateByPrimaryKey(detail);
                //您发起的全民鉴定【名称】已结束投票，点击查看最终结果吧
                schedualMessageService.easemobMessage(detail.getUserId().toString(),
                        "您发起的全民鉴定【"+detail.getTitle()+"】已结束投票，点击查看最终结果吧","7","1",detail.getId().toString());
            }
        }
    }

    @Override
    public void dailyWage() throws ServiceException {
        //每天上午08:00 结算专栏返利，200新增浏览量（当前日期前一天0时到24时新增浏览量）/元，浏览量为奇数时，浏览量-1再计算返利金额，直接返到用户余额，并提示用户，新增余额账单
        List<Map<String,Object>> forumPvs = forumPvMapper.dailyWage();
        for(Map forumPv:forumPvs){
            ForumColumn forumColumn = forumColumnMapper.selectByPrimaryKey((int)forumPv.get("columnId"));
            long count = (long)forumPv.get("count");
            if(count % 2 != 0){
                //奇数
                count-=1;
            }
            BigDecimal amount = new BigDecimal(count).multiply(new BigDecimal(0.005)).setScale(2,BigDecimal.ROUND_HALF_UP);
            schedualWalletService.updateBalance(forumColumn.getUserId(),amount,Status.ADD.getValue());
            //订单号
            final IdGenerator idg = IdGenerator.INSTANCE;
            String id = idg.nextId();
            schedualWalletService.addUserBalanceDetail(forumColumn.getUserId(),amount,Status.PAY_TYPE_BALANCE.getValue(),Status.EXPEND.getValue(),id,"专栏每日收益",null,forumColumn.getUserId(),Status.FORUM_COLUMN.getValue());
            schedualMessageService.easemobMessage(forumColumn.getId().toString(),"您的专栏本日收益为"+amount+"元！已发放至您的余额，点击此处查看您的余额吧","1","13","");
        }
    }
}
