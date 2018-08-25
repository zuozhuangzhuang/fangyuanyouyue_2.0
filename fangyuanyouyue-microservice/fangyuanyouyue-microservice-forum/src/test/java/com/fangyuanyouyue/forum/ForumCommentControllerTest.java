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
public class ForumCommentControllerTest {
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
     * 帖子评论列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void forumComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/commentList")
                .param("token", "10045FY1534451510951")
                .param("forumId", "1")
                .param("start", "0")
                .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 帖子二级评论列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void forumCommentComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/replyList")
                .param("token", "10045FY1534451510951")
                .param("commentId", "1")
                .param("start", "0")
                .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 添加评论
     * @throws Exception
     */
    @Test
    @Transactional
    public void saveComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/add")
                .param("token", "10045FY1534451510951")
                .param("forumId", "10")
                .param("content", "0")
                .param("commentId", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 帖子评论点赞
     * @throws Exception
     */
    @Test
    @Transactional
    public void commentLikes() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/comment/likes")
                .param("token", "10045FY1534451510951")
                .param("commentId", "1")
                //类型 1点赞 2取消点赞
                .param("type", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 我的帖子、视频评论列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void myComments() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/myComments")
                .param("token", "10045FY1534843098025")
                .param("start", "0")
                .param("limit", "10")
                //类型 1帖子 2视频
                .param("type", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
