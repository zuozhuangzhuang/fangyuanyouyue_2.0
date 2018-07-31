package com.fangyuanyouyue.goods;

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
@SpringBootTest(classes = GoodsServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class AppraisalControllerTest {
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
     * 申请鉴定
     * @throws Exception
     */
    @Test
    @Transactional
    public void addAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/addAppraisal")
                .param("token","10025FY1531851479276")
                .param("goodsIds","12")
                //鉴定类型 1商家鉴定 2买家
                .param("type","2")
                //鉴定标题
                .param("title","鉴定一下")
                //鉴定赏金
                .param("price","10.5")
                //描述
                .param("description","我想鉴定一下，这是我家传的宝贝")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
