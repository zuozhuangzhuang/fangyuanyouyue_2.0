package com.fangyuanyouyue.user.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.admin.AdminOperatorDto;
import com.fangyuanyouyue.user.param.AdminOperatorParam;

public interface SysOperatorService {
    
	/**
	 * 获取全部操作员
	 * @return
	 * @throws ServiceException
	 */
    List<AdminOperatorDto> getAllOperator() throws ServiceException;


	/**
	 * 获取角色全部操作员
	 * @return
	 * @throws ServiceException
	 */
    List<AdminOperatorDto> getAllOperator(Integer roleId) throws ServiceException;
    
    /**
     * 删除操作员
     * @param menuId
     */
    void deleteOperator(Integer operatorId) ;
    
    /**
     * 保存操作员
     * @param param
     * @throws ServiceException 
     */
    void saveOperator(AdminOperatorParam param) throws ServiceException;
    
    /**
     * 登录
     * @param loginCode
     * @param loginPwd
     * @return
     * @throws ServiceException
     */
    AdminOperatorDto login(String loginCode,String loginPwd) throws ServiceException;
    
}
