package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.admin.AdminDailyStatisticsDto;
import com.fangyuanyouyue.user.dto.admin.AdminProcessDto;
import com.fangyuanyouyue.user.param.AdminUserParam;

import java.util.List;

public interface SystemService {
    /**
     * 用户反馈
     * @param userId
     * @param content
     * @param type
     * @param version
     * @throws ServiceException
     */
    void feedback(Integer userId,String content,Integer type,String version) throws ServiceException;

    /**
     * 意见反馈列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager feedbackList(AdminUserParam param) throws ServiceException;

    /**
     * 发送系统消息
     * @param content
     * @throws ServiceException
     */
    void sendMessage(Integer userId,String content) throws ServiceException;

    /**
     * 获取统计信息
     * @return
     * @throws ServiceException
     */
    AdminProcessDto getProcess() throws ServiceException;

    /**
     * 每小时统计一次今日注册用户
     * @throws ServiceException
     */
    Integer processTodayUser() throws ServiceException;

    /**
     * 每小时统计一次总注册用户
     * @throws ServiceException
     */
    Integer processAllUser() throws ServiceException;

    /**
     * 每天统计一次本月注册用户
     * @throws ServiceException
     */
    Integer processYesterdayUser() throws ServiceException;

    /**
     * 系统消息历史列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager sysMsgList(AdminUserParam param) throws ServiceException;

    /**
     * 获取统计列表
     * @param count
     * @return
     * @throws ServiceException
     */
    List<AdminDailyStatisticsDto> getProcessList(Integer count) throws ServiceException;

    /**
     * 
     * @param id
     * @param type
     * @return
     * @throws ServiceException
     */
    String getQRCode(Integer id,Integer type) throws ServiceException;
}
