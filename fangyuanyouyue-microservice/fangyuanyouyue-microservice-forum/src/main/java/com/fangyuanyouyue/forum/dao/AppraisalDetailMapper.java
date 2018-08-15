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
     * 获取用户收藏的鉴定列表
     * @param userId
     * @param start
     * @param limit
     * @param collectType
     * @return
     */
    List<AppraisalDetail> selectCollectList(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("collectType")Integer collectType);
}