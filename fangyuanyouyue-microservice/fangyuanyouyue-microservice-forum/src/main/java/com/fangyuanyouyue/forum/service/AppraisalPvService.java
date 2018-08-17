package com.fangyuanyouyue.forum.service;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface AppraisalPvService {

    
    /**
     * 计算点赞数量
     * @param appraisalId
     * @return
     */
    Integer countPv(Integer appraisalId);
    
    /**
     * 
     * @param userId
     * @param appraisalId
     * @param type
     */
    void savePv(Integer userId,Integer appraisalId);

}
