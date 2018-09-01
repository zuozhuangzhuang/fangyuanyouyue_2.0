package com.fangyuanyouyue.forum.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface TimerService {
    /**
     * 结算全民鉴定
     * @throws ServiceException
     */
    void appraisalEnd() throws ServiceException;
}
