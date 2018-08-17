package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppraisalDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalDetail record);

    int insertSelective(AppraisalDetail record);

    AppraisalDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalDetail record);

    int updateByPrimaryKeyWithBLOBs(AppraisalDetail record);

    int updateByPrimaryKey(AppraisalDetail record);

    /**
     * 我发起的、全民鉴定列表
     * @param userId
     * @param keyword
     * @param start
     * @param limit
     * @return
     */
    List<AppraisalDetail> selectMyList(@Param("userId")Integer userId,@Param("keyword")String keyword,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 我参与的
     * @param userId
     * @param keyword
     * @param start
     * @param limit
     * @return
     */
    List<AppraisalDetail> selectListWithMe(@Param("userId")Integer userId,@Param("keyword")String keyword,@Param("start")Integer start,@Param("limit")Integer limit);


    /**
     * 获取用户收藏的鉴定列表
     * @param userId
     * @param start
     * @param limit
     * @param collectType
     * @param search
     * @return
     */
    List<AppraisalDetail> selectCollectList(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("collectType")Integer collectType,@Param("search")String search);

    /**
     * 获取鉴定详情（包括用户头像、昵称）
     * @param id
     * @return
     */
    AppraisalDetail selectDetailByPrimaryKey(Integer id);
}
