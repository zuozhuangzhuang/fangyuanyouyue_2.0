package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.model.IdentityAuthApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IdentityAuthApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdentityAuthApply record);

    int insertSelective(IdentityAuthApply record);

    IdentityAuthApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdentityAuthApply record);

    int updateByPrimaryKey(IdentityAuthApply record);

    IdentityAuthApply certification(int userId, String name, String identity, String identity_img_cover, String identity_img_back) throws ServiceException;

    /**
     * 根据用户ID和状态值获取用户实名认证申请记录
     * @param userId
     * @return
     */
    IdentityAuthApply selectByUserId(@Param("userId")Integer userId);


    /**
     * 分页总条数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @return
     */
    List<IdentityAuthApply> getPage(@Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}