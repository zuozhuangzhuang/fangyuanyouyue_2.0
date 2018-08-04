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
public class BargainControllerTest {

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
     * 商品压价申请
     * @throws Exception
     */
    @Test
//    @Transactional
    public void addBargain() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bargain/addBargain")
                .param("token","10036FY1533190696203")
                .param("goodsId","85")
                .param("price","800")
                .param("reason","交个朋友")
                .param("addressId","45")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 处理压价
     * @throws Exception
     */
    @Test
//    @Transactional
    public void updateBargain() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bargain/updateBargain")
                .param("token","10025FY1533144562288")
                .param("goodsId","85")
                .param("bargainId","15")
                //状态 2同意 3拒绝 4取消
                .param("status","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 我的压价列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void bargainList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bargain/bargainList")
                .param("token","10025FY1533144562288")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
