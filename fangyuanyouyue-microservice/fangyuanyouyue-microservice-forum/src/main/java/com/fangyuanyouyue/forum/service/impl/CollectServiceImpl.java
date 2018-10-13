package com.fangyuanyouyue.forum.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.AppraisalCommentMapper;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.AppraisalImgMapper;
import com.fangyuanyouyue.forum.dao.AppraisalLikesMapper;
import com.fangyuanyouyue.forum.dao.CollectMapper;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dao.ForumImgMapper;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dao.ForumLikesMapper;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.dto.AppraisalImgDto;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.model.AppraisalComment;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.AppraisalImg;
import com.fangyuanyouyue.forum.model.Collect;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.model.ForumLikes;
import com.fangyuanyouyue.forum.service.CollectService;
import com.fangyuanyouyue.forum.service.ForumCommentService;
import com.fangyuanyouyue.forum.service.ForumLikesService;
import com.fangyuanyouyue.forum.service.ForumPvService;
import com.fangyuanyouyue.forum.service.SchedualUserService;

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
    @Autowired
    private AppraisalLikesMapper appraisalLikesMapper;
    @Autowired
    private AppraisalCommentMapper appraisalCommentMapper;
    @Autowired
    private AppraisalImgMapper appraisalImgMapper;
    @Autowired
    private ForumLikesService forumLikesService;
    @Autowired
    private ForumImgMapper forumImgMapper;
    @Autowired
    private ForumCommentService forumCommentService;
    @Autowired
    private ForumPvService forumPvService;

    @Override
    public void collect(Integer userId, Integer collectId, Integer collectType,Integer status) throws ServiceException {
        if(collectType == 3){//视频
            ForumInfo forumInfo = forumInfoMapper.selectByPrimaryKey(collectId);
            if(forumInfo == null || Status.HIDE.getValue().equals(forumInfo.getStatus())){
                throw new ServiceException("收藏对象不存在！");
            }
        }else if (collectType == 4) {//帖子
            ForumInfo forumInfo = forumInfoMapper.selectByPrimaryKey(collectId);
            if(forumInfo == null || Status.HIDE.getValue().equals(forumInfo.getStatus())){
                throw new ServiceException("收藏对象不存在！");
            }
        }else if(collectType == 5){//鉴定
            AppraisalDetail appraisalDetail = appraisalDetailMapper.selectByPrimaryKey(collectId);
            if(appraisalDetail == null || Status.DELETE.getValue().equals(appraisalDetail.getStatus())){
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
        List dtos = new ArrayList();
        if(collectType.intValue() == 3 || collectType.intValue() == 4){
            List<ForumInfo> forumInfos = forumInfoMapper.selectCollectList(userId, start * limit, limit, collectType,collectType == 4?1:2,search);
            for(ForumInfo info:forumInfos) {
                ForumInfoDto dto = new ForumInfoDto(info);
                //是否收藏
                Collect collect = collectMapper.selectByCollectIdType(userId, info.getId(), info.getType() == 2?3:4);
                if(collect != null){
                    dto.setIsCollect(StatusEnum.YES.getValue());
                }
                //计算点赞数
                Integer likesCount = forumLikesService.countLikes(info.getId());
                dto.setLikesCount(likesCount);

                //计算评论数
                Integer commentCount = forumCommentService.countComment(info.getId());
                dto.setCommentCount(commentCount);
                //是否点赞
                ForumLikes forumLikes = forumLikesMapper.selectByForumIdUserId(dto.getForumId(), userId);
                if(forumLikes != null){
                    dto.setIsLikes(StatusEnum.YES.getValue());
                }
                //浏览量
                Integer pvCount = forumPvService.countPv(info.getId());
                dto.setViewCount(pvCount+info.getPvCount());
                dtos.add(dto);
            }
        }else if(collectType.intValue() == 5){
            List<AppraisalDetail> appraisalDetails = appraisalDetailMapper.selectCollectList(userId, start * limit, limit,collectType,search);
            //列表不需要返回点赞数、浏览量、评论量
            for(AppraisalDetail model:appraisalDetails) {
                AppraisalDetailDto dto = new AppraisalDetailDto(model);
                //是否收藏
                Collect collect = collectMapper.selectByCollectIdType(userId, model.getId(), 5);
                if(collect != null){
                    dto.setIsCollect(StatusEnum.YES.getValue());
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
        }else{
            throw new ServiceException("收藏类型错误！");
        }
        return dtos;
    }



}
