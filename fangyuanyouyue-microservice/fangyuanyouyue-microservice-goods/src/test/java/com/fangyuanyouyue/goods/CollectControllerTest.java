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
    @Transactional
    public void collectGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/collect/collectGoods")
                .param("token","10025FY1532052608373")
                //收藏对象ID
                .param("collectId","1，2，3")
                //类型 1关注 2收藏
                .param("type","2")
                //关注/收藏类型 关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏
                .param("collectType","1")
                //状态 1关注/收藏 2取消关注/收藏
                .param("status","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 收藏/关注
     * @throws Exception
     */
    @Test
    @Transactional
    public void collectList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/collect/collectList")
                .param("token","10025FY1531851479276")
                //类型 1关注 2收藏
                .param("type","1")
                //关注/收藏类型 关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏
                .param("collectType","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
