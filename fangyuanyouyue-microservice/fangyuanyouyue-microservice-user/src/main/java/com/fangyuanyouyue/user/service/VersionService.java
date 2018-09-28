package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.AppVersionDto;
import com.fangyuanyouyue.user.dto.admin.AdminAppVersionDto;
import com.fangyuanyouyue.user.param.AdminUserParam;

public interface VersionService {
    /**
     * 版本更新
     * @return
     * @throws ServiceException
     */
    AppVersionDto getVersion() throws ServiceException;

    /**
     * 版本更新列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager getVersionList(AdminUserParam param) throws ServiceException;

    /**
     * 新增版本
     * @param param
     * @throws ServiceException
     */
    void versionAdd(AdminUserParam param) throws ServiceException;

    /**
     * 修改版本信息
     * @param param
     * @throws ServiceException
     */
    void versionModify(AdminUserParam param) throws ServiceException;

    /**
     * 删除版本
     * @param versionId
     * @throws ServiceException
     */
    void versionDelete(Integer versionId) throws ServiceException;
}
