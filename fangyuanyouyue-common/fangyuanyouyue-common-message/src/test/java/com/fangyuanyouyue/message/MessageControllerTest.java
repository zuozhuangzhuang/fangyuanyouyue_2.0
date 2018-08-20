package com.fangyuanyouyue.message;

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
@SpringBootTest(classes = MessageServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class MessageControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void sendCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/message/sendCode")
                .param("phone","18103966057")
                .param("type","0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void easemobMessage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/message/easemob/message")
                .param("userName","45")
                .param("content","您的商品【商品名称】、【xxx】、【xx】买家已申请退货，点击此处处理一下吧")
                .param("type","txt")
                .param("businessId","922")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
