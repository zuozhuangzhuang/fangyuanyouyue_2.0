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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ForumServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class AppraisalControllerTest {
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
     * 全民鉴定详情
     * @throws Exception
     */
    @Test
//    @Transactional
    public void appraisalDetail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/detail")
                .param("token", "10045FY1534482216917")
                .param("appraisalId", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 全民鉴定列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void appraisalList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/list")
                .param("token", "10045FY1534482216917")
                //关键字
                .param("keyword", "")
                .param("start", "0")
                .param("limit", "20")
                //类型 1我参与的 2我发起的
                .param("type", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 全民鉴定评论列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void commentList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/comment/list")
                .param("token", "10045FY1534482216917")
                .param("appraisalId", "")
                .param("start", "0")
                .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 全民鉴定点赞
     * @throws Exception
     */
    @Test
//    @Transactional
    public void likes() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/likes")
                .param("token", "10045FY1534482216917")
                .param("appraisalId", "")
                //类型 1点赞 2取消点赞
                .param("type", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 全民鉴定评论点赞
     * @throws Exception
     */
    @Test
//    @Transactional
    public void commentLikes() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/likes")
                .param("token", "10045FY1534482216917")
                .param("commentId", "")
                //类型 1点赞 2取消点赞
                .param("type", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 发起鉴定
     * @throws Exception
     */
    @Test
//    @Transactional
    public void addAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/addAppraisal")
                .param("token", "10045FY1534482216917")
                .param("bonus", "10")
                .param("title", "来看看这个咸丰")
                .param("content", "刚入手一枚好看的钱，大家帮我看看是真的还是假的")
                .param("imgUrls", "123,124,125")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 添加评论
     * @throws Exception
     */
    @Test
//    @Transactional
    public void saveComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/saveComment")
                .param("token", "10045FY1534482216917")
                .param("appraisalId", "10")
                //评论观点 1看真 2看假
                .param("viewpoint", "1")
                .param("content", "真的")
                //邀请用户数组
                .param("userIds", "16")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
