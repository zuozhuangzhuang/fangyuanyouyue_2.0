package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;
import java.util.List;

import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dao.ForumLikesMapper;
import com.fangyuanyouyue.forum.dto.ForumLikesDto;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.model.ForumLikes;
import com.fangyuanyouyue.forum.service.ForumLikesService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;


@Service(value = "forumLikesService")
@Transactional(rollbackFor=Exception.class)
public class ForumLikesServiceImpl implements ForumLikesService {

    @Autowired
    private ForumLikesMapper forumLikesMapper;
    @Autowired
	private SchedualWalletService schedualWalletService;
    @Autowired
	private ForumInfoMapper forumInfoMapper;
    
	@Override
	public Integer countLikes(Integer forumId) {
		return forumLikesMapper.countById(forumId);
	}

	@Override
	public List<ForumLikesDto> getLikesList(Integer forumId, Integer start, Integer limit) throws ServiceException {
		List<ForumLikes> list = forumLikesMapper.selectByForumId(forumId, start*limit, limit);
		return ForumLikesDto.toDtoList(list);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@TxTransaction(isStart=true)
	public void saveLikes(Integer type, Integer userId, Integer forumId) throws ServiceException{
		ForumLikes model = forumLikesMapper.selectByForumIdUserId(forumId,userId);
		if(type == 1){
			if(model != null){
				throw new ServiceException("您已点赞，请勿重复点赞！");
			}else{
				model = new ForumLikes();
				model.setUserId(userId);
				model.setForumId(forumId);
				model.setAddTime(new Date());
				forumLikesMapper.insert(model);
				ForumInfo forumInfo = forumInfoMapper.selectByPrimaryKey(forumId);
				//新增用户行为
				BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBehavior(userId,forumInfo.getUserId(),forumId, Status.BUSINESS_TYPE_FORUM.getValue(),Status.BEHAVIOR_TYPE_LIKES.getValue()));
				if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(baseResp.getCode(),baseResp.getReport());
				}
			}
		}else if(type == 2){
			if(model == null){
				throw new ServiceException("未点赞，请先点赞！");
			}else{
				forumLikesMapper.deleteByPrimaryKey(model.getId());
			}
		}else{
			throw new ServiceException("类型错误！");
		}

	}

}
