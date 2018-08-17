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
public class CollectControllerTest {
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
     * 收藏/关注
     * @throws Exception
     */
    @Test
//    @Transactional
    public void collect() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/collect/collect")
                .param("token","10045FY1534236594430")
                //收藏对象ID
                .param("collectId","1")
                //状态 1收藏 2取消收藏
                .param("status","1")
                //关注/收藏类型 收藏类型  3视频 4专栏 5鉴赏
                .param("collectType","3")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 我的收藏列表:视频
     * @throws Exception
     */
    @Test
//    @Transactional
    public void collectFroumColumnList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/collect/collectList")
                .param("token","10045FY1534451510951")
                //关注/收藏类型  3视频 4专栏 5鉴定
                .param("collectType","3")
                .param("start","0")
                .param("limit","10")
                .param("search","测试")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 我的收藏列表:帖子
     * @throws Exception
     */
    @Test
//    @Transactional
    public void collectFroumList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/collect/collectList")
                .param("token","10045FY1534451510951")
                //关注/收藏类型  3视频 4帖子 5鉴定
                .param("collectType","4")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 我的收藏列表:鉴定
     * @throws Exception
     */
    @Test
//    @Transactional
    public void collectAppraisalList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/collect/collectList")
                .param("token","10045FY1534236594430")
                //关注/收藏类型  3视频 4专栏 5鉴定
                .param("collectType","5")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
