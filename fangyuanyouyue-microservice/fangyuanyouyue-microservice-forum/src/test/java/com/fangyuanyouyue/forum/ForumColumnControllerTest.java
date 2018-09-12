package com.fangyuanyouyue.forum;

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
@SpringBootTest(classes = ForumServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class ForumColumnControllerTest {
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
     * 获取全部专栏
     * @throws Exception
     */
    @Test
    @Transactional
    public void columnList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/column/list")
                .param("typeId","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    

    /**
     * 获取推荐专栏
     * @throws Exception
     */
    @Test
    @Transactional
    public void columnChosen() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/column/chosen")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 发布专栏
     * @throws Exception
     */
    @Test
//    @Transactional
    public void addColumn() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/column/addColumn")
                .param("token", "10045FY1534183918247")
                .param("typeId", "8")
                .param("name", "女人都是大猪蹄子")

                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 我是栏主
     * @throws Exception
     */
    @Test
//    @Transactional
    public void myColumn() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/column/myColumn")
                .param("token", "10045FY1535479286901")
                .param("start", "0")
                .param("limit", "10")

                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


}
