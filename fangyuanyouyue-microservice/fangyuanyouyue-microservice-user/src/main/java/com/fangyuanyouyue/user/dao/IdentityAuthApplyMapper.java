package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.model.IdentityAuthApply;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdentityAuthApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdentityAuthApply record);

    int insertSelective(IdentityAuthApply record);

    IdentityAuthApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdentityAuthApply record);

    int updateByPrimaryKey(IdentityAuthApply record);

    IdentityAuthApply certification(int userId, String name, String identity, String identity_img_cover, String identity_img_back) throws ServiceException;

    IdentityAuthApply selectByUserId(Integer userId);
}