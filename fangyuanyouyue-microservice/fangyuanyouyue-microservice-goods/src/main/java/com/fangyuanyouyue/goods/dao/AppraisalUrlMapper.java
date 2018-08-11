package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.AppraisalUrl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppraisalUrlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalUrl record);

    int insertSelective(AppraisalUrl record);

    AppraisalUrl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalUrl record);

    int updateByPrimaryKey(AppraisalUrl record);

    /**
     * 根据鉴定ID获取图片列表
     * @param detailId
     * @return
     */
    List<AppraisalUrl> selectListBuUserId(@Param("detailId")Integer detailId);
}