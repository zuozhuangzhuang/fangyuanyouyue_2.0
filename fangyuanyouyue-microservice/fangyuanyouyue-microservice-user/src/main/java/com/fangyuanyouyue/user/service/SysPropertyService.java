package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.SysPropertyDto;
import com.fangyuanyouyue.user.param.AdminUserParam;

public interface SysPropertyService {
    /**
     * 根据规则类型获取规则信息
//     * @param ruleKey
     * @return
     * @throws ServiceException
     */
    SysPropertyDto getInviteRule() throws ServiceException;

    /**
     * 后台查看所有规则
     * @return
     * @throws ServiceException
     */
    Pager getPage(AdminUserParam param) throws ServiceException;

    /**
     * 后台新增规则
     * @param ruleContent
     * @param ruleKey
     * @throws ServiceException
     */
    void addRule(String ruleContent,String ruleKey) throws ServiceException;

    /**
     * 后台修改规则内容
     * @param id
     * @param ruleContent
     * @param ruleKey
     * @throws ServiceException
     */
    void updateRule(Integer id,String ruleContent,String ruleKey) throws ServiceException;

    /**
     * 后台删除规则
     * @param id
     * @throws ServiceException
     */
    void deleteRule(Integer id) throws ServiceException;
}
