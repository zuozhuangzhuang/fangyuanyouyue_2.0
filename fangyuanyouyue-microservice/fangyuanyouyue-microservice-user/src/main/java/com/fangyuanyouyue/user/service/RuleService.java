package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.RuleDto;
import com.fangyuanyouyue.user.param.AdminUserParam;

public interface RuleService {
    /**
     * 根据规则类型获取规则信息
     * @param ruleType
     * @return
     * @throws ServiceException
     */
    RuleDto getInviteRule(Integer ruleType) throws ServiceException;

    /**
     * 后台查看所有规则
     * @return
     * @throws ServiceException
     */
    Pager getPage(AdminUserParam param) throws ServiceException;

    /**
     * 后台新增规则
     * @param ruleContent
     * @param ruleType
     * @throws ServiceException
     */
    void addRule(String ruleContent,Integer ruleType) throws ServiceException;

    /**
     * 后台修改规则内容
     * @param id
     * @param ruleContent
     * @throws ServiceException
     */
    void updateRule(Integer id,String ruleContent) throws ServiceException;

    /**
     * 后台删除规则
     * @param id
     * @throws ServiceException
     */
    void deleteRule(Integer id) throws ServiceException;
}
