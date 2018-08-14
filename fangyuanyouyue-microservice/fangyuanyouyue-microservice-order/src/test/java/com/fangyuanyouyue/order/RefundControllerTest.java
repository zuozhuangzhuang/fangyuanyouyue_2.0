package com.fangyuanyouyue.order;

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
@SpringBootTest(classes = OrderServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class RefundControllerTest {
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
     * 退货申请
     * @throws Exception
     */
    @Test
//    @Transactional
    public void cancelOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/refund/orderReturnToSeller")
                .param("token","10042FY1533767510392")
                .param("orderId","918")
                .param("reason","坏的")
//                .param("imgUrls","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 退货列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void orderReturnList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/refund/orderReturnList")
                .param("token","10042FY1533767510392")
                .param("start","0")
                .param("limit","10")
                //类型 1买家（我买下的） 2卖家（我卖出的）
                .param("type","1")
//                .param("imgUrls","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 退货列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void handleReturns() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/refund/handleReturns")
                .param("token","10045FY1534151532158")
                .param("orderId","947")
//                .param("reason","")
                //处理状态 2同意 3拒绝
                .param("status","2")
//                .param("imgUrls","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
