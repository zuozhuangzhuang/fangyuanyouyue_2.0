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
//    @Transactional
    public void addAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/addAppraisal")
//                .param("token","10041FY1533753292042")
                .param("token","10025FY1533837647461")
                .param("goodsIds","1,2")
                //描述
//                .param("description","我想鉴定一下，这是我家传的宝贝")
//                .param("imgUrls","123,124,125,126")
//                .param("videoUrl","127")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 取消鉴定
     * @throws Exception
     */
    @Test
//    @Transactional
    public void cancelAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/cancelAppraisal")
                .param("token","10041FY1533753292042")
                .param("orderId","27")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 鉴定查询
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/getAppraisal")
                .param("token","10041FY1533753292042")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 鉴定结果
     * @throws Exception
     */
    @Test
//    @Transactional
    public void appraisalDetail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/appraisalDetail")
                .param("token","10041FY1533753292042")
                .param("detailId","78")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 鉴定支付
     * @throws Exception
     */
    @Test
//    @Transactional
    public void payAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/appraisal/payAppraisal")
                .param("token","10041FY1533753292042")
                .param("orderId","26")
                .param("payPwd","e10adc3949ba59abbe56e057f20f883e")
                //支付方式 1支付宝 2微信 3余额支付
                .param("type","3")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 鉴定展示
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getAllAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/appraisal/getAllAppraisal")
                .param("start","0")
                .param("limit","10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
