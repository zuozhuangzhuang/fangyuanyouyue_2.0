package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.*;
import com.fangyuanyouyue.forum.model.AppraisalComment;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service(value = "timerService")
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


    @Override
    public void appraisalEnd() throws ServiceException {
        //1、获取status=1，enndTime小于当前时间的全民鉴定 2、获取鉴定中评论获取点赞最多的评论，定为获胜者(票数一致最先评论者胜出) 3、向发起者和获胜者发送消息通知
        List<AppraisalDetail> details = appraisalDetailMapper.selectByStatusEndTime(1, DateStampUtils.getTimesteamp());
        if(details != null && details.size() > 0){
            for(AppraisalDetail detail:details){
                List<AppraisalComment> comments= appraisalCommentMapper.selectByAppraisalId(detail.getId(), null, null);
                for(int i=0;i<comments.size();i++){
                    AppraisalComment comment = comments.get(i);
                    if(i==0){
                        //第一个胜出
                        comment.setIsWinner(StatusEnum.YES.getValue());
                        appraisalCommentMapper.updateByPrimaryKey(comment);
                        //胜者增加余额
                        if(detail.getBonus() != null){
                            BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(comment.getUserId(),detail.getBonus(),1)), BaseResp.class);
                            if(baseResp.getCode() == 1){
                                throw new ServiceException(baseResp.getReport().toString());
                            }
                        }
                        //恭喜您！您参与的全民鉴定【名称】，您获得了最高票数！点击查看最终结果吧~
                        schedualMessageService.easemobMessage(detail.getUserId().toString(),
                                "恭喜您！您参与的全民鉴定【"+detail.getTitle()+"】，您获得了最高票数！点击查看最终结果吧~","7","1",detail.getId().toString());
                    }else{
                        comment.setIsWinner(StatusEnum.NO.getValue());
                        appraisalCommentMapper.updateByPrimaryKey(comment);
                        //您参与的全民鉴定【名称】已结束投票，点击查看最终结果吧
                        schedualMessageService.easemobMessage(detail.getUserId().toString(),
                                "您参与的全民鉴定【"+detail.getTitle()+"】已结束投票，点击查看最终结果吧","7","1",detail.getId().toString());
                    }
                }
                detail.setStatus(2);
                appraisalDetailMapper.updateByPrimaryKey(detail);
                //您发起的全民鉴定【名称】已结束投票，点击查看最终结果吧
                schedualMessageService.easemobMessage(detail.getUserId().toString(),
                        "您发起的全民鉴定【"+detail.getTitle()+"】已结束投票，点击查看最终结果吧","7","1",detail.getId().toString());
            }
        }
    }
}
