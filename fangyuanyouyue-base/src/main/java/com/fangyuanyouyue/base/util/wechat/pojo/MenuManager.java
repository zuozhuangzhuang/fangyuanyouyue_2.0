package com.fangyuanyouyue.base.util.wechat.pojo;


import com.fangyuanyouyue.base.util.wechat.utils.WeixinUtil;
import org.apache.log4j.Logger;

/** 
 * 菜单管理器类 
 *  
 * @author wuzhimin 
 * @date 2013-08-08 
 */
public class MenuManager {  
	
	private static Logger log = Logger.getLogger(WeixinUtil.class);
       
    public static void main(String[] args) {  
        // 第三方用户唯一凭证  
        String appId = "wx102c4e20d1ca70c3";  
        // 第三方用户唯一凭证密钥  
        String appSecret = "bd794995931c7ebf6b1bd824257613a5";  
       
        // 调用接口获取access_token  
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret); 
        System.out.println("获取到的token为："+at.getToken());
        System.out.println("token有效期为："+at.getExpiresIn());
       
        if (null != at) {  
            // 调用接口创建菜单  
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());  
       
            // 判断菜单创建结果  
            if (0 == result) {
                log.info("菜单创建成功！");
            } else {
                log.info("菜单创建失败，错误码：" + result);
            }
        }  
        
    }  
    
       
    /** 
     * 组装菜单数据 
     *  
     * @return 
     */
    private static Menu getMenu() {  
      
        /*CommonButton btn21 = new CommonButton();  
        btn21.setName("订座");  
        btn21.setType("view");  
        btn21.setUrl("http://test.chuangshiyiming.com/yeseweixin/room_list.html");
       
        CommonButton btn22 = new CommonButton();  
        btn22.setName("订酒水");  
        btn22.setType("view");  
        btn22.setUrl("http://test.chuangshiyiming.com/yeseweixin/drinks.html");

        CommonButton btn23 = new CommonButton();  
        btn23.setName("购物车");  
        btn23.setType("view");  
        btn23.setUrl("http://test.chuangshiyiming.com/yeseweixin/bar_shopping_cart.html");
       
       
        CommonButton btn31 = new CommonButton();  
        btn31.setName("订单");  
        btn31.setType("view");  
        btn31.setUrl("http://test.chuangshiyiming.com/yeseweixin/bar_my_order.html");
       
        CommonButton btn32 = new CommonButton();  
        btn32.setName("个人中心");  
        btn32.setType("view");  
        btn32.setUrl("http://test.chuangshiyiming.com/yeseweixin/personal_center.html");*/
        
        ComplexButton mainBtn1 = new ComplexButton();  
        mainBtn1.setName("最新活动");  
        mainBtn1.setType("view");  
        mainBtn1.setUrl("http://test.chuangshiyiming.com/yeseweixin/ktv_details.html");
      //  mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14 ,btn15});  
       
        ComplexButton mainBtn2 = new ComplexButton();  
        mainBtn2.setName("订座");  
        mainBtn2.setType("view");  
        mainBtn2.setUrl("http://test.chuangshiyiming.com/yeseweixin/ktv_details.html");
     //   mainBtn2.add(btn21);
     //   mainBtn2.add(btn22);
     //   mainBtn2.add(btn23);
      //  mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23  });  
       
        ComplexButton mainBtn3 = new ComplexButton();  
        mainBtn3.setName("我的");  
        mainBtn3.setType("view");  
        mainBtn3.setUrl("http://test.chuangshiyiming.com/yeseweixin/ktv_details.html");
   //     mainBtn3.add(btn31);
   //     mainBtn3.add(btn32);
    //    mainBtn3.setSub_button(new CommonButton[] { btn31, btn32}); */ 
       
        /** 
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br> 
         *  
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br> 
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br> 
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
         */
        Menu menu = new Menu();  
        menu.add(mainBtn1);
        menu.add(mainBtn2);
        menu.add(mainBtn3);
     //   menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });  
       
        return menu;  
    }  
}