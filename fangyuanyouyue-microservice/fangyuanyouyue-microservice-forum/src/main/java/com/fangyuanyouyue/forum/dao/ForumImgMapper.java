package com.fangyuanyouyue.forum.dao;

import java.util.List;

import com.fangyuanyouyue.forum.model.ForumImg;
import org.apache.ibatis.annotations.Mapper;

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