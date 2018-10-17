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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ForumServiceApplication.class)
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
     * 获取举报列表
     * @throws Exception
     */
    @Test
//    @Transactional
    public void reportList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminForum/reportList")
                .param("type", "1")
                .param("start", "0")
                .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改帖子、视频
     * @throws Exception
     */
    @Test
//    @Transactional
    public void updateForum() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/adminForum/updateForum")
                .param("id", "50")
                .param("sort", "1")
//                .param("isChosen", "")
//                .param("status", "")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 编辑浏览量基数
     * @throws Exception
     */
    @Test
//    @Transactional
    public void uploadForum() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/adminForum/uploadForum")
                .param("filePath", "/Users/wuzhimin/Desktop/unzip")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    
    
    /**
     * 编辑浏览量基数
     * @throws Exception
     */
    @Test
//    @Transactional
    public void updateFansCount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/adminForum/updateFansCount")
                .param("id", "130")
                .param("count", "100000")
                .param("type", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    

}
