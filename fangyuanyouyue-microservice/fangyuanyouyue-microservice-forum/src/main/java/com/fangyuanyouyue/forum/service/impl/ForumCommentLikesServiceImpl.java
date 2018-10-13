package com.fangyuanyouyue.forum.service.impl;

import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.dao.ForumCommentLikesMapper;
import com.fangyuanyouyue.forum.dao.ForumCommentMapper;
import com.fangyuanyouyue.forum.model.ForumComment;
import com.fangyuanyouyue.forum.model.ForumCommentLikes;
import com.fangyuanyouyue.forum.service.ForumCommentLikesService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;


@Service(value = "forumCommentLikesService")
@Transactional(rollbackFor=Exception.class)
public class ForumCommentLikesServiceImpl implements ForumCommentLikesService {

    @Autowired
    private ForumCommentLikesMapper forumCommentLikesMapper;
    @Autowired
	private SchedualWalletService schedualWalletService;
    @Autowired
	private ForumCommentMapper forumCommentMapper;
    
	@Override
	public Integer countLikes(Integer commentId) {
		return forumCommentLikesMapper.countById(commentId);
	}

	@Override
	@Transactional
	@TxTransaction(isStart=true)
	public void saveLikes(Integer type, Integer userId, Integer commentId)  throws ServiceException {
		ForumCommentLikes forumCommentLikes = forumCommentLikesMapper.selectByUserIdCommentId(userId, commentId);
		if(type == 1){
			if(forumCommentLikes != null){
				throw new ServiceException("您已点赞，请勿重复点赞！");
			}else{
				forumCommentLikes = new ForumCommentLikes();
				forumCommentLikes.setUserId(userId);
				forumCommentLikes.setCommentId(commentId);
				forumCommentLikes.setAddTime(DateStampUtils.getTimesteamp());
				forumCommentLikesMapper.insert(forumCommentLikes);
				ForumComment forumComment = forumCommentMapper.selectByPrimaryKey(commentId);
				BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBehavior(userId,forumComment.getUserId(),commentId, Status.BUSINESS_TYPE_APPRAILSA_COMMENT.getValue(), Status.BEHAVIOR_TYPE_LIKES.getValue()));
				if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(baseResp.getCode(),baseResp.getReport());
				}
			}
		}else if(type == 2){
			if(forumCommentLikes == null){
				throw new ServiceException("未点赞，请先点赞！");
			}else{
				forumCommentLikesMapper.deleteByPrimaryKey(forumCommentLikes.getId());
			}
		}else{
			throw new ServiceException("类型错误！");
		}
	}

}
