package com.fangyuanyouyue.goods;

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
@SpringBootTest(classes = GoodsServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class CommentControllerTest {
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
     * 发布评论/回复
     * @throws Exception
     */
    @Test
    @Transactional
    public void addComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/addComment")
                //用户token：notNull
                .param("token","10025FY1531851479276")
                //商品id：notNull
                .param("goodsId","1")
                //回复评论id
                .param("commentId","")
                //评论内容：notNull
                .param("content","这是啥！")
                //图片地址1
                .param("img1Url","")
                //图片地址2
                .param("img2Url","")
                //图片地址3
                .param("img3Url","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 评论点赞
     * @throws Exception
     */
    @Test
    @Transactional
    public void commentLikes() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/commentLikes")
                .param("token","10025FY1532974762055")
                //评论id
                .param("commentId","2")
                //类型 1商品 2抢购
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 查看全部评论
     * @throws Exception
     */
    @Test
    @Transactional
    public void getComments() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/getComments")
                //商品ID
                .param("goodsId","1")
                .param("start","0")
                .param("limit","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * （商品/抢购）我的评论
     * @throws Exception
     */
    @Test
    @Transactional
    public void myComments() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/myComments")
                //商品ID
                .param("token","10025FY1533144562288")
                //类型 1商品 2抢购
                .param("type","2")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 批量删除评论
     * @throws Exception
     */
    @Test
    @Transactional
    public void deleteComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment/deleteComment")
                .param("token","10025FY1533317806918")
                //评论id
                .param("commentIds","166,167")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
