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
public class CartControllerTest {

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
     * 添加商品到购物车
     * @throws Exception
     */
    @Test
    @Transactional
    public void addGoodsToCart() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/cart/addGoodsToCart")
                .param("token","10025FY1533144562288")
                .param("goodsId","32")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 我的购物车
     * @throws Exception
     */
    @Test
    @Transactional
    public void getCart() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/cart/getCart")
                .param("token","10025FY1533317806918")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 移出购物车
     * @throws Exception
     */
    @Test
    @Transactional
    public void cartRemove() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/cart/cartRemove")
                .param("token","10025FY1533144562288")
                .param("cartDetailIds","37,38")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 精选
     * @throws Exception
     */
    @Test
    @Transactional
    public void choice() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/cart/choice")
                .param("token","10025FY1533144562288")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
