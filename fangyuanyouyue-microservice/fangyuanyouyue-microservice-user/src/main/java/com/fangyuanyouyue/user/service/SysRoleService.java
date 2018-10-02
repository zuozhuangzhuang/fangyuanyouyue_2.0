package com.fangyuanyouyue.user.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.admin.AdminRoleDto;
import com.fangyuanyouyue.user.param.AdminRoleParam;

public interface SysRoleService {
    
	/**
	 * 获取全部菜单
	 * @return
	 * @throws ServiceException
	 */
    List<AdminRoleDto> getAllRole() throws ServiceException;

	/**
	 * 获取拥有菜单的角色
	 * @return
	 * @throws ServiceException
	 */
    List<AdminRoleDto> getRoleByMenuId(Integer menuId) throws ServiceException;

	/**
	 * 获取用户角色
	 * @return
	 * @throws ServiceException
	 */
    List<AdminRoleDto> getRoleByUserId(Integer userId) throws ServiceException;
    
	/**
	 * 保存角色
	 * @throws ServiceException
	 */
    void saveRole(AdminRoleParam param) throws ServiceException;
    
    /**
     * 删除角色
     * @param menuId
     */
    void deleteRole(Integer roleId) ;
    
}
