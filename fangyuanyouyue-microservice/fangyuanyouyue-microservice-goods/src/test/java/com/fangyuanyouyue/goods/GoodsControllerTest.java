package com.fangyuanyouyue.goods;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GoodsServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class GoodsControllerTest {
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
    @Transactional
    public void goodsList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/goods/goodsList")
                .param("token","10025FY1533317806918")
//                .param("userId","1")
                //商品状态 普通商品 1出售中 2已售出 3已下架（已结束） 5删除
//                .param("status","1")
                .param("start","0")
                .param("limit","10")
//                .param("search","")
                //综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序
//                .param("synthesize","2")
//                .param("priceMin","10")
//                .param("priceMax","1000")
                //品质 1：认证店铺 2：官方保真 3：高信誉度 4.我的关注 5：(已完成)已完成
//                .param("quality","5")
                //类型 1普通商品 2抢购商品
                .param("type","1")
//                .param("goodsCategoryIds", "23,121,5")

                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 发布商品/抢购
     * @throws Exception
     */
    @Test
//    @Transactional
    public void addGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/goods/addGoods")
                .param("token","10041FY1533753292042")
                .param("goodsInfoName","试一下视频截图路径")
                .param("goodsCategoryIds","10,22")
                .param("description","试一下视频截图路径")
                .param("price","1000")
                .param("postage","10")
                .param("label","1")
//                .param("floorPrice","")
//                .param("intervalTime","")
//                .param("markdown","")
                .param("type","1")
                .param("status","1")
                .param("imgUrls","http://app.fangyuanyouyue.com/static/pic/default/001.jpg,http://app.fangyuanyouyue.com/static/pic/default/002.jpg")
                .param("videoUrl","videoUrlllllllllllllll")
                .param("videoImg","videoImggggggggggggggg")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 批量删除商品
     * @throws Exception
     */
    @Test
//    @Transactional
    public void deleteGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/goods/deleteGoods")
                .param("token","10025FY1534118693031")
                .param("goodsIds","86")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 商品详情
     * @throws Exception
     */
    @Test
    @Transactional
    public void goodsInfo() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/goodsInfo")
                .param("token","10042FY1533767510392")
                .param("goodsId","175")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 获取分类列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void categoryList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/categoryList")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 同类推荐
     * @throws Exception
     */
    @Test
    @Transactional
    public void similarGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/similarGoods")
                .param("goodsId","1")
                .param("start","0")
                .param("limit","4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取首页轮播图
     * @throws Exception
     */
    @Test
    @Transactional
    public void getBanner() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/getBanner")
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 热门搜索
     * @throws Exception
     */
    @Test
    @Transactional
    public void hotSearch() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/hotSearch")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 热门分类
     * @throws Exception
     */
    @Test
    @Transactional
    public void hotCategary() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/hotCategary")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取快速查询条件
     * @throws Exception
     */
    @Test
    @Transactional
    public void quickSearch() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/goods/quickSearch")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 获取快速查询条件
     * @throws Exception
     */
    @Test
    @Transactional
    public void reportGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/goods/reportGoods")
                .param("token","10025FY1532302815762")
                .param("goodsId","1")
                .param("reason","假货")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
