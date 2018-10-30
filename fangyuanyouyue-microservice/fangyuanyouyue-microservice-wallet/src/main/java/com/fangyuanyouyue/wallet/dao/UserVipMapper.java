package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserVip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserVipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVip record);

    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);

    /**
     * 根据userId获取用户会员信息
     * @param userId
     * @return
     */
    UserVip selectByUserId(@Param("userId")Integer userId);

    /**
     * 获取该结束的会员列表
     * @param endTime
     * @return
     */
    List<UserVip> selectByEndTime(@Param("endTime")Date endTime);

    /**
     * 查询所有年费会员
     * @return
     */
    List<UserVip> selectUserVipYear();

    /**
     * 分页总条数
     * @param vipLevel
     * @param vipType
     * @param isSendMessage
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("vipLevel")Integer vipLevel,@Param("vipType")Integer vipType,@Param("isSendMessage")Integer isSendMessage,@Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取会员列表
     * @param vipLevel
     * @param vipType
     * @param isSendMessage
     * @param start
     * @param limit
     * @param keyword
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<UserVip> getPage(@Param("vipLevel")Integer vipLevel,@Param("vipType")Integer vipType,@Param("isSendMessage")Integer isSendMessage, @Param("start") Integer start,
                              @Param("limit") Integer limit, @Param("keyword")String keyword,
                              @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);

    /**
     * 获取需要重置置顶次数的会员数据
     * @return
     */
    List<UserVip> getResetFreeTopCountList();
}