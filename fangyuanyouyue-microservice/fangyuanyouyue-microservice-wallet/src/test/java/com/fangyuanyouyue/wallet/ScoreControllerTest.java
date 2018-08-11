package com.fangyuanyouyue.wallet;

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
@SpringBootTest(classes = WalletServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class ScoreControllerTest {
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
     * 获取奖池信息
     * @throws Exception
     */
    @Test
    @Transactional
    public void getBonusPool() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/score/getBonusPool")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 抽奖
     * @throws Exception
     */
    @Test
//    @Transactional
    public void lottery() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/score/lottery")
                .param("token","10025FY1533837647461")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
