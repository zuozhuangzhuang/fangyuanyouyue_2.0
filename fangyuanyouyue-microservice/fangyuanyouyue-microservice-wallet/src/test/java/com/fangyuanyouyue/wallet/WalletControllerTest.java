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
public class WalletControllerTest {
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
     * 充值
     * @throws Exception
     */
    @Test
    @Transactional
    public void recharge() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/wallet/recharge")
                .param("token","10025FY1533317806918")
                .param("price","100")
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 提现
     * @throws Exception
     */
    @Test
    @Transactional
    public void withdrawDeposit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/wallet/withdrawDeposit")
                .param("token","10025FY1533317806918")
                .param("price","")
                .param("type","")
                .param("account","")
                .param("realName","")
                .param("payPwd","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取用户钱包信息
     * @throws Exception
     */
    @Test
    @Transactional
    public void getWallet() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/wallet/getWallet")
                .param("token","10025FY1533317806918")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
