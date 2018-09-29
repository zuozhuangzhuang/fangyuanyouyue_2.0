package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface CollectService {
    /**
     * 收藏或取消
     * @param userId
     * @param collectId
     * @param collectType
     * @param status
     * @throws ServiceException
     */
    void collect(Integer userId, Integer collectId, Integer collectType, Integer status) throws ServiceException;

    /**
     * 我的收藏列表 (视频、专栏、鉴定)
     * @param userId
     * @param collectType
     * @param start
     * @param limit
     * @param search
     * @return
     * @throws ServiceException
     */
    List collectList(Integer userId, Integer collectType, Integer start, Integer limit,String search) throws ServiceException;
}
