package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumImg;

@Mapper
public interface ForumImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumImg record);

    int insertSelective(ForumImg record);

    ForumImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumImg record);

    int updateByPrimaryKey(ForumImg record);
    
    List<ForumImg> selectByForumId(Integer forumId);
}