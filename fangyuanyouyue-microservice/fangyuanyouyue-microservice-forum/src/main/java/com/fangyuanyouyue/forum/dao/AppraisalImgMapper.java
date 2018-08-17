package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppraisalImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalImg record);

    int insertSelective(AppraisalImg record);

    AppraisalImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalImg record);

    int updateByPrimaryKey(AppraisalImg record);

    /**
     * 获取鉴定图片列表
     * @param appraisalId
     * @return
     */
    List<AppraisalImg> selectListByAppraisal(@Param("appraisalId")Integer appraisalId);
}