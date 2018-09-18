package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.BonusPoolDto;

import java.util.List;

public interface ScoreService {
    /**
     * 奖池
     * @return
     * @throws ServiceException
     */
    List<BonusPoolDto> getBonusPool() throws ServiceException;

    /**
     * 抽奖
     * @param userId
     * @return
     * @throws ServiceException
     */
    String lottery(Integer userId) throws ServiceException;

    /**
     * 分享增加积分
     * @param userId
     * @throws ServiceException
     */
    void shareHtml(Integer userId) throws ServiceException;


    /**
     * 修改积分
     * @param userId
     * @param score
     * @param type 1 增加 2减少
     * @throws ServiceException
     */
    void updateScore(Integer userId,Long score,Integer type) throws ServiceException;
}
