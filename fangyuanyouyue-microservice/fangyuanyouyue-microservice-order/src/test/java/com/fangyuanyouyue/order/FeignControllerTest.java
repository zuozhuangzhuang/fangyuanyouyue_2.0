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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class FeignControllerTest {
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
     * 统计今日订单
     * @throws Exception
     */
    @Test
//    @Transactional
    public void processTodayOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/orderFeign/processTodayOrder")
//                .param("status","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 统计总订单
     * @throws Exception
     */
    @Test
//    @Transactional
    public void processAllOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/orderFeign/processAllOrder")
//                .param("status","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

//    /**
//     * 每天统计一次本月订单
//     * @throws Exception
//     */
//    @Test
////    @Transactional
//    public void processMonthOrder() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.post("/timer/processMonthOrder")
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }
}
