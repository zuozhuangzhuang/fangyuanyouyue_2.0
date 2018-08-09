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
}
