package com.fangyuanyouyue.forum.service;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 全民鉴定点赞接口
 * @author wuzhimin
 *
 */
public interface AppraisalLikesService {

 
    /**
     * 获取点赞数量
     * @param id
     * @return
     * @throws ServiceException
     */
    Integer countLikes(Integer id) throws ServiceException;
    

    
    /**
     * 保存点赞
     * @param userId
     * @param appraisalId
     * @throws ServiceException
     */
    void saveLikes(Integer userId,Integer appraisalId) throws ServiceException;

}
