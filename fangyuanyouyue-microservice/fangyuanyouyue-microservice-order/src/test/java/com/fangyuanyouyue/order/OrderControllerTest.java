package com.fangyuanyouyue.order;

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
@SpringBootTest(classes = OrderServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class OrderControllerTest {
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
     * 商品下单
     * @throws Exception
     */
    @Test
//    @Transactional
    public void saveOrderByCart() throws Exception {
        String a = "[{\"sellerId\":16,\"addOrderDetailDtos\":[{\"goodsId\":2,\"couponId\":10}]}]";
        String b = "[{\"sellerId\":25,\"addOrderDetailDtos\":[{\"goodsId\":101},{\"goodsId\":102}]},{\"sellerId\":24,\"addOrderDetailDtos\":[{\"goodsId\":33}]}]";
        String c = "[{\"sellerId\":39,\"addOrderDetailDtos\":[{\"goodsId\":140},{\"goodsId\":141},{\"goodsId\":142}]}]";
        mvc.perform(MockMvcRequestBuilders.post("/order/saveOrderByCart")
                .param("token","10045FY1534556954408")
                .param("sellerList",c)
                .param("addressId","58")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 取消订单
     * @throws Exception
     */
    @Test
    @Transactional
    public void cancelOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/cancelOrder")
                .param("token","10025FY1532136940739")
                .param("orderId","6")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 订单详情
     * @throws Exception
     */
    @Test
    @Transactional
    public void orderDetail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/orderDetail")
                .param("token","10036FY1533354566271")
                .param("orderId","846")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 我的订单列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void myOrderList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/myOrderList")
                .param("token","10042FY1534495936222")
                .param("start","0")
                .param("limit","100")
                //类型 1买家（我买下的） 2卖家（我卖出的）
                .param("type","1")
                //订单状态 0全部 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                .param("status","0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 商品/抢购直接下单
     * @throws Exception
     */
    @Test
//    @Transactional
    public void saveOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/saveOrder")
                .param("token","10036FY1533354566271")
                .param("goodsId","98")
                //优惠券ID
                .param("couponId","")
                .param("addressId","45")
                //类型 1普通商品 2抢购商品
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 订单支付
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getOrderPay() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/getOrderPay")
                .param("token","10041FY1533753292042")
                .param("orderId","921")
                //支付方式 1支付宝 2微信 3余额支付
                .param("type","3")
                .param("payPwd","e10adc3949ba59abbe56e057f20f883e")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 卖家确认发货
     * @throws Exception
     */
    @Test
//    @Transactional
    public void sendGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/sendGoods")
                .param("token","10041FY1533753292042")
                .param("orderId","909")
                .param("companyId","")
                //物流号
                .param("number","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 买家确认收货
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getGoods() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/getGoods")
                .param("token","10041FY1533753292042")
                .param("orderId","921")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 物流公司
     * @throws Exception
     */
    @Test
//    @Transactional
    public void companyList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/companyList")
                .param("token","10041FY1533753292042")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 删除订单
     * @throws Exception
     */
    @Test
//    @Transactional
    public void deleteOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/deleteOrder")
                .param("token","10042FY1533767510392")
                .param("orderIds","919")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 评价卖家
     * @throws Exception
     */
    @Test
//    @Transactional
    public void evaluationOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/order/evaluationOrder")
                .param("token","10042FY1533767510392")
                .param("orderId","919")
                .param("goodsQuality","3")
                .param("serviceAttitude","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
