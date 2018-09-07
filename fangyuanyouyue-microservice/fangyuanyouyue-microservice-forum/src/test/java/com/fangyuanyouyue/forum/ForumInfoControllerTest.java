package com.fangyuanyouyue.forum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ForumServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class ForumInfoControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    /**
     * 获取全部专栏
     * @throws Exception
     */
    @Test
    @Transactional
    public void forumList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/forum/list")
                .param("token", "10015FY1536222536781")
//                .param("userId", "42")
//                .param("columnId", "1")
//        		.param("keyword", "")
                //搜索类型1古物圈子 2专栏精选
//        		.param("searchType", "")
                //帖子类型 1帖子 2视频
                .param("type", "1")
                .param("start", "0")
                .param("limit", "10")
                //列表类型 1普通列表 2我的帖子/视频列表
                .param("listType", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 帖子详情
     * @throws Exception
     */
    @Test
    @Transactional
    public void forumDetail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/forum/detail")
                .param("token", "10045FY1534451510951")
                .param("forumId", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 发布视频
     * @throws Exception
     */
    @Test
//    @Transactional
    public void addForum() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/forum/addForum")
        		.param("token", "10045FY1534451510951")
        		.param("columnId", "4")
        		.param("title", "这是一个帖子")
        		.param("content", "这个帖子主要是为了测试一下发布帖子接口能不能用")
//        		.param("title", "这是一个视频")
//        		.param("content", "这个视频主要是为了测试一下发布视频接口能不能用")
//        		.param("videoUrl", "123123123")
//        		.param("videoLength", "300")
//        		.param("videoImg", "123123123")
                //帖子类型 1帖子 2视频
        		.param("type", "1")
//        		.param("userIds", "")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }




}
