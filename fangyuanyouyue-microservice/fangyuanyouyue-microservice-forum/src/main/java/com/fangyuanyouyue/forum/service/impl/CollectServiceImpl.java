package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.*;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.model.*;
import com.fangyuanyouyue.forum.service.CollectService;
import com.fangyuanyouyue.forum.service.ForumLikesService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "collectService")
@Transactional(rollbackFor=Exception.class)
public class CollectServiceImpl implements CollectService{
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private AppraisalDetailMapper appraisalDetailMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private ForumColumnMapper forumColumnMapper;
    @Autowired
    private ForumInfoMapper forumInfoMapper;
    @Autowired
    private ForumLikesMapper forumLikesMapper;

    @Override
    public void collect(Integer userId, Integer collectId, Integer collectType,Integer status) throws ServiceException {
        if(collectType == 3){//视频
            ForumInfo forumInfo = forumInfoMapper.selectByPrimaryKey(collectId);
            if(forumInfo == null){
                throw new ServiceException("收藏对象不存在！");
            }else{
                //帖子不能收藏
                if(forumInfo.getType() == 1){
                    throw new ServiceException("类型错误！");
                }
            }
        }else if (collectType == 4) {//专栏
            ForumColumn forumColumn = forumColumnMapper.selectByPrimaryKey(collectId);
            if(forumColumn == null){
                throw new ServiceException("收藏对象不存在！");
            }
        }else if(collectType == 5){//鉴定
            AppraisalDetail appraisalDetail = appraisalDetailMapper.selectByPrimaryKey(collectId);
            if(appraisalDetail == null){
                throw new ServiceException("收藏对象不存在！");
            }
        }else{
            throw new ServiceException("收藏类型错误！");
        }
        Collect collect = collectMapper.selectByCollectIdType(userId, collectId, collectType);
        //status        : 状态 1发起 2取消
        //collectType   : 收藏类型 3视频 4帖子
        if (status == 1) {
            if (collect == null) {
                collect = new Collect();
                collect.setAddTime(DateStampUtils.getTimesteamp());
                collect.setCollectId(collectId);
                collect.setCollectType(collectType);
                collect.setType(2);
                collect.setUserId(userId);
                collectMapper.insert(collect);
            } else {
                throw new ServiceException("已收藏，请勿重新收藏！");
            }
        } else if (status == 2) {
            if (collect == null) {
                throw new ServiceException("未收藏，请先收藏！");
            } else {
                collectMapper.deleteByPrimaryKey(collect.getId());
            }
        } else {
            throw new ServiceException("状态错误！");
        }
    }

    @Override
    public List collectList(Integer userId, Integer collectType,Integer start, Integer limit,String search) throws ServiceException {
        //collectType 收藏类型 3视频 4帖子 5鉴赏
        //type 类型 1关注 2收藏
        List dtos;
        if(collectType.intValue() == 3 || collectType.intValue() == 4){
            List<ForumInfo> forumInfos = forumInfoMapper.selectCollectList(userId, start * limit, limit, collectType,collectType == 4?1:2,search);
            dtos = ForumInfoDto.toDtoList(forumInfos);
            for(ForumInfoDto dto:(List<ForumInfoDto>)dtos){
                //获取卖家信息
                UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(dto.getUserId())).getString("data")), UserInfo.class);
                dto.setNickName(user.getNickName());
                dto.setHeadImgUrl(user.getHeadImgUrl());
                //是否点赞
                ForumLikes forumLikes = forumLikesMapper.selectByForumIdUserId(dto.getForumId(), userId);
                if(forumLikes != null){
                    dto.setIsLikes(StatusEnum.YES.getValue());
                }
            }
        }else if(collectType.intValue() == 5){
            List<AppraisalDetail> appraisalDetails = appraisalDetailMapper.selectCollectList(userId, start * limit, limit,collectType);
            dtos = AppraisalDetailDto.toDtoList(appraisalDetails);
        }else{
            throw new ServiceException("收藏类型错误！");
        }
        return dtos;
    }



}
