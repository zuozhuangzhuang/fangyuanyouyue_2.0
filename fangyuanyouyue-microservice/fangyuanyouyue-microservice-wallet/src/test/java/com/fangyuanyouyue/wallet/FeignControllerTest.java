package com.fangyuanyouyue.wallet;

import com.fangyuanyouyue.base.enums.NotifyUrl;
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
public class FeignControllerTest {
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
     * 修改积分
     * @throws Exception
     */
    @Test
    @Transactional
    public void updateScore() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/updateScore")
                .param("userId","25")
                .param("score","200")
                //类型 1 增加 2减少
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改余额
     * @throws Exception
     */
    @Test
    @Transactional
    public void updateBalance() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/updateBalance")
                .param("userId","25")
                .param("amount","200")
                //类型 1充值 2消费
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改信誉度
     * @throws Exception
     */
    @Test
    @Transactional
    public void updateCredit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/updateCredit")
                .param("userId","25")
                .param("credit","200")
                //类型 1增加 2减少
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取免费鉴定次数
     * @throws Exception
     */
    @Test
    @Transactional
    public void getAppraisalCount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/getAppraisalCount")
                .param("userId","25")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改剩余免费鉴定次数
     * @throws Exception
     */
    @Test
    @Transactional
    public void updateAppraisalCount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/updateAppraisalCount")
                .param("userId","25")
                .param("count","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 微信支付
     * @throws Exception
     */
    @Test
    @Transactional
    public void orderPayByWechat() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/orderPayByWechat")
                .param("orderNo","25")
                .param("price","100")
                //回调地址
                .param("notifyUrl",NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.order_wechat_notify.getNotifUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 支付宝支付
     * @throws Exception
     */
    @Test
    @Transactional
    public void orderPayByALi() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/orderPayByALi")
                .param("orderNo","25")
                .param("price","1.00")
                //回调地址
                .param("notifyUrl", NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.order_wechat_notify.getNotifUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 新增用户收支信息
     * @throws Exception
     */
    @Test
    @Transactional
    public void addUserBalanceDetail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/addUserBalanceDetail")
                .param("userId","25")
                .param("amount","10.00")
                //支付类型 1微信 2支付宝 3余额
                .param("payType","1")
                //收支类型 1收入 2支出
                .param("type","1")
                .param("orderNo","")
                .param("title","")
                .param("sellerId","")
                .param("buyerId","")
                //订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5申请专栏 6充值 7开通、续费会员 8认证店铺
                .param("orderType", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 根据优惠券id计算价格
     * @throws Exception
     */
    @Test
    @Transactional
    public void getPriceByCoupon() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/walletFeign/getPriceByCoupon")
                .param("userId","25")
                .param("price","200")
                .param("couponId","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
