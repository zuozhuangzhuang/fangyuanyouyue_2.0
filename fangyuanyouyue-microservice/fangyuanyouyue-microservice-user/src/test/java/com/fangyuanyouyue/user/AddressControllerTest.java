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
public class AddressControllerTest {
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
     * 添加收货地址
     * @throws Exception
     */
    @Test
    @Transactional
    public void addAddress() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/address/addAddress")
                .param("token","10014FY1531254449019")
                .param("receiverName","左壮壮")
                .param("receiverPhone","18103966057")
                .param("province","广东省")
                .param("city","深圳市")
                .param("area","罗湖区")
                .param("address","世界金融中心B座1015")
                .param("postCode","450000")
                .param("type","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改收货地址
     * @throws Exception
     */
    @Test
    @Transactional
    public void updateAddress() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/address/updateAddress")
                .param("token","10014FY1531254449019")
                .param("addressId","1")
                .param("receiverName","左壮壮")
                .param("receiverPhone","18103966057")
                .param("province","广东省")
                .param("city","深圳市")
                .param("area","罗湖区")
                .param("address","世界金融中心B座1015")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 删除收货地址
     * @throws Exception
     */
    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/address/deleteAddress")
                .param("token","10014FY1531254449019")
                .param("addressId","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取收货地址列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void getAddressList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/address/getAddressList")
                .param("token","10014FY1531254449019")
                .param("addressId","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 设置默认收货地址
     * @throws Exception
     */
    @Test
    @Transactional
    public void defaultAddress() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/address/defaultAddress")
                .param("userId","1")
                .param("addressId","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
