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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class SystemControllerTest {
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
     * 意见反馈
     * @throws Exception
     */
    @Test
//    @Transactional
    public void feedback() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/system/feedback")
                .param("token","10025FY1537214751447")
                .param("content","不会用")
                //类型 1安卓 2iOS 3小程序
                .param("type","2")
                .param("version","1.4.1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取统计信息
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getProcess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/system/getProcess")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取统计信息
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getProcessList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/system/getProcessList")
                .param("count","7")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取小程序二维码
     * @throws Exception
     */
    @Test
    public void getQRCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/system/getQRCode")
                .param("token","116418FY1544238047739")
                .param("id","106418")
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
