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
public class AdminUserControllerTest {
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
     * 修改实名认证状态
     * @throws Exception
     */
    @Test
//    @Transactional
    public void authApplyStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/adminUser/auth/status")
                .param("id","1982")
                .param("status","1")
                .param("content","重新提交")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 意见反馈列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void feedbackList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminUser/feedbackList")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 用户昵称修改记录表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void nickNameList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminUser/nickNameList")
                .param("id","101")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 编辑用户粉丝基数
     * @throws Exception
     */
    @Test
//    @Transactional
    public void updateFansCount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminUser/updateFansCount")
                .param("id","25")
                .param("count","1000")
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
