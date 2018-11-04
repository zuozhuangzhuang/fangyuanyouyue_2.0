package com.fangyuanyouyue.user;

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

import java.io.FileInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@Rollback
public class UserControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    /*@Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
    }*/

//    perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
//    andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；
//    andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台；
//    andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；
//    accept：指定请求的Accept头信息；

    /**
     * 注册
     * @throws Exception
     */
    @Test
    @Transactional
    public void regist() throws Exception {

            mvc.perform(MockMvcRequestBuilders.post("/user/regist")
                    .param("phone","13333333")
                    .param("loginPwd","e10adc3949ba59abbe56e057f20f883e")
                    .param("nickName","昵称")
    //                .param("headImgUrl","123456")
    //                .param("bgImgUrl","123456")
                    .param("gender","1")
                    .param("regPlatform","1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
    }

    /**
     * 用户登录
     * @throws Exception
     */
    @Test
//    @Transactional
    public void login() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .param("phone","181039660571")
                .param("loginPwd","123456")
                .param("loginPlatform","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * APP三方注册/登录
     * @throws Exception
     */
    @Test
    @Transactional
    public void thirdLogin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/thirdLogin")
                .param("thirdNickName","昵称")
                //第三方账号头像地址
//                .param("thirdHeadImgUrl","")
                //性别，1男 2女 0不确定
                .param("gender","1")
                //登录平台 1安卓 2iOS 3小程序
                .param("loginPlatform","1")
                .param("unionId","oJ9SjwsSoaWNMR_xflHnyaRnUf2Q")
                //类型 1微信 2QQ 3微博
                .param("type","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 三方绑定
     * @throws Exception
     */
    @Test
    @Transactional
    public void thirdBind() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/thirdBind")
                .param("token","10025FY1536135283895")
                .param("unionId","D7B2FB693AA3B2F28793D656303C9B28")
                //类型 1微信 2QQ 3微博
                .param("type","2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 实名认证
     * @throws Exception
     */
    @Test
    @Transactional
    public void certification() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/certification")
                .param("token","10025FY1533317806918")
                .param("name","左壮壮")
                //身份证号
                .param("identity","41282419940411771X")
                //身份证封面图路径
//                .param("identityImgCoverUrl","")
                //身份证背面路径
//                .param("identityImgBackUrl","")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 完善资料
     * @throws Exception
     */
    @Test
    @Transactional
    public void modify() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/modify")
                .param("token","10016FY1536143745761")
                .param("phone","181039660571")
//                .param("email","zuozhuang_zzz@163.com")
                //用户所在地
//                .param("userAddress","zuozhuang_zzz@163.com")
//                .param("nickName","偷看看哟")
                //头像图片路径
//                .param("headImgUrl","偷看看哟")
                //背景图片路径
//                .param("bgImgUrl","偷看看哟")
                //性别，1男 2女 0不确定
//                .param("gender","1")
                //个性签名
//                .param("signature","个性签名")
                //联系电话
//                .param("contact","13333333333")
                //身份证号码
//                .param("identity","41282419940411772X")
                //真实姓名
//                .param("name","左壮壮")
                //支付密码，md5加密，32位小写字母
//                .param("payPwd","123456")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 找回密码
     * @throws Exception
     */
    @Test
    @Transactional
    public void resetPwd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/resetPwd")
                .param("phone","18103966057")
                .param("newPwd","123456")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改密码
     * @throws Exception
     */
    @Test
    @Transactional
    public void updatePwd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/updatePwd")
                .param("token","10014FY1531254449019")
                .param("loginPwd","123456")
                .param("newPwd","654321")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 修改绑定手机
     * @throws Exception
     */
    @Test
    @Transactional
    public void updatePhone() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/updatePhone")
                .param("token","1")
                .param("phone","18103966056")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 小程序登录
     * @throws Exception
     */
    @Test
    @Transactional
    public void miniLogin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/miniLogin")
                //code值
                .param("code","1")
                //第三方账号昵称
                .param("thirdNickName","18103966056")
                //第三方账号头像地址
                .param("thirdHeadImgUrl","18103966056")
                //性别，1男 2女 0不确定
                .param("gender","18103966056")
                //类型 1微信 2QQ 3微博
                .param("type","18103966056")
                //包括敏感数据在内的完整用户信息的加密数据
                .param("encryptedData","18103966056")
                //加密算法的初始向量
                .param("iv","18103966056")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 发送验证码
     * @throws Exception
     */
    @Test
    @Transactional
    public void sendCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/sendCode")
                .param("token","1")
                .param("phone","18103966056")
                //验证码类型 0表示注册 1表示密码找回 2 表示支付密码相关 3验证旧手机，4绑定新手机 5店铺认证
                .param("type","18103966056")
                .param("unionId","18103966056")
                //类型 1微信 2QQ 3微博
                .param("thirdType","18103966056")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 验证验证码
     * @throws Exception
     */
    @Test
    @Transactional
    public void verifyCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/verifyCode")
                //验证码
                .param("code","8870")
                .param("phone","18103966057")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取个人店铺列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void shopList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/shopList")
//                .param("nickName","佛")
                .param("start","0")
                .param("limit","10")
//                .param("authType","1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取用户信息
     * @throws Exception
     */
    @Test
    @Transactional
    public void userInfo() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/userInfo")
                .param("token","10025FY1532677840708")
                .param("userId","22")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 添加/取消关注
     * @throws Exception
     */
    @Test
    @Transactional
    public void fansFollow() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/fansFollow")
                .param("token","10025FY1531699708772")
                .param("toUserId","21")
                .param("type","0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 我的粉丝/我的关注
     * @throws Exception
     */
    @Test
    @Transactional
    public void myFansOrFollows() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/myFansOrFollows")
                .param("token","118351FY1539236097872")
                //类型 1我的关注 2我的粉丝
                .param("type","1")
                .param("start","0")
                .param("limit","10")
//                .param("search","师弟")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取待处理信息
     * @throws Exception
     */
    @Test
    @Transactional
    public void myWaitProcess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/myWaitProcess")
                .param("token","10041FY1533753292042")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 申请官方认证
     * @throws Exception
     */
    @Test
    @Transactional
    public void authType() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/authType")
                .param("token","")
                //支付方式 1微信 2支付宝 3余额 4小程序
                .param("payType","3")
                .param("payPwd","123456")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 根据用户名获取用户列表
     * @throws Exception
     */
    @Test
    @Transactional
    public void getUserByName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/getUserByName")
                .param("search","")
                .param("start","0")
                .param("limit","20")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }





    /**
     * 获取合并账号用户信息
     * @throws Exception
     */
    @Test
//    @Transactional
    public void getMergeUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/getMergeUser")
                .param("token","10091FY1536445350902")
                .param("phone","18103966056")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 获取合并账号用户信息
     * @throws Exception
     */
    @Test
//    @Transactional
    public void accountMerge() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/accountMerge")
                .param("token","10041FY1536550905200")
                .param("phone","18103966056")
                .param("loginPwd","111111")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
