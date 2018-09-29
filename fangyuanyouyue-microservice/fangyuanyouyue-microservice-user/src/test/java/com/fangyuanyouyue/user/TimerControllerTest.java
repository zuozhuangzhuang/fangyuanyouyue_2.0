package com.fangyuanyouyue.user;

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
@SpringBootTest(classes = UserServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class TimerControllerTest {
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
     * 官方认证自动过期
     * @throws Exception
     */
//    @Test
//    @Transactional
//    public void shopAuthTimeOut() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.post("/timer/shopAuthTimeOut")
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }

    /**
     * 每小时统计一次今日注册用户
     * @throws Exception
     */
    @Test
//    @Transactional
    public void processTodayUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/timer/processTodayUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 每小时统计一次总注册用户
     * @throws Exception
     */
    @Test
//    @Transactional
    public void processAllUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/timer/processAllUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 每天统计一次本月注册用户
     * @throws Exception
     */
    @Test
//    @Transactional
    public void processMonthUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/timer/processMonthUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
