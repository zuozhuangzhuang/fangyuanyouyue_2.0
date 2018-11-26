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
public class UserVipControllerTest {
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
     * 开通/续费会员
     * @throws Exception
     */
    @Test
//    @Transactional
    public void updateMember() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/userVip/updateMember")
                .param("token","116418FY1543173760806")
                //会员等级 1铂金会员 2至尊会员
                .param("vipLevel","2")
                //会员类型 1一个月 2三个月 3一年会员
                .param("vipType","2")
                //类型 1开通 2续费
                .param("type","2")
                //支付方式  1微信 2支付宝 3余额 4小程序
                .param("payType","3")
                .param("payPwd","e10adc3949ba59abbe56e057f20f883e")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    /**
     * 获取用户剩余会员时长价值金额
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getVipWorth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/userVip/getVipWorth")
                .param("token","116418FY1543173760806")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
