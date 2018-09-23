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
public class AdminControllerTest {
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
     * 获取商品列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void goodsList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminGoods/goodsList")
//                .param("type","")
                .param("start","0")
                .param("limit","10")
//                .param("keyword","")
                //状态 1已处理 2待处理
//                .param("status","")
//                .param("startDate","2016-01-01 01:01:01")
//                .param("endDate","2018-09-12 16:39:00")
//                .param("orders","add_time")
//                .param("ascType","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 后台处理举报
     * @throws Exception
     */
    @Test
//    @Transactional
    public void dealReport() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/adminGoods/dealReport")
                .param("id","2")
                .param("content","假货！")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取举报商品列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void reportList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminGoods/reportList")
                .param("type","")
                .param("start","0")
                .param("limit","10")
                .param("keyword","")
                //状态 1已处理 2待处理
                .param("status","")
                .param("startDate","2016-01-01 01:01:01")
                .param("endDate","2018-09-12 16:39:00")
                .param("orders","add_time")
                .param("ascType","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 官方鉴定列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void appraisalList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminGoods/appraisalList")
//                .param("type","0")
                .param("start","0")
                .param("limit","10")
//                .param("keyword","")
                //状态 1已处理 2待处理
//                .param("status","")
//                .param("startDate","2016-01-01 01:01:01")
//                .param("endDate","2018-09-12 16:39:00")
//                .param("orders","add_time")
//                .param("ascType","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 后台处理官方鉴定
     * @throws Exception
     */
    @Test
//    @Transactional
    public void updateAppraisal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/adminGoods/updateAppraisal")
                .param("id","79")
                .param("content","真的")
                .param("status","1")
                .param("isShow","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
