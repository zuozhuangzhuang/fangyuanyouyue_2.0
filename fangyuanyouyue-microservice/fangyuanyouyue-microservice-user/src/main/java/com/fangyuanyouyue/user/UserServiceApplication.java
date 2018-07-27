package com.fangyuanyouyue.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@RefreshScope
@EnableFeignClients
@Configuration
public class UserServiceApplication {
	/**
	 * ************************************************************************
	 * **                              _oo0oo_                               **
	 * **                             o8888888o                              **
	 * **                             88" . "88                              **
	 * **                             (| -_- |)                              **
	 * **                             0\  =  /0                              **
	 * **                           ___/'---'\___                            **
	 * **                        .' \\\|     |// '.                          **
	 * **                       / \\\|||  :  |||// \\                        **
	 * **                      / _ ||||| -:- |||||- \\                       **
	 * **                      | |  \\\\  -  /// |   |                       **
	 * **                      | \_|  ''\---/''  |_/ |                       **
	 * **                      \  .-\__  '-'  __/-.  /                       **
	 * **                    ___'. .'  /--.--\  '. .'___                     **
	 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
	 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
	 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
	 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
	 * **                              '=---='                               **
	 * ************************************************************************
	 * **                        佛祖保佑      启动正常                      **
	 * ************************************************************************
	 *
	 */
/*
	                ####        .,::::,,####,::::::.        W###i        #W#######j     ###.
                    ####        j##################t        W###i        #########fG##; ###.
                    ####        t##################t        W###i        ffj###Wi, D##; ###.
            .##################   .    .W###         ##################:    ###D   G##; ###.
            .##################.        ####         W#################. ffj###Kfj:G##; ###.
            :EDDDDDD####DDDDDDE         ####.        EDDDDDE####EDEDDDD  #########jG##; ###.
                    ####         ;################;         ####t       .#########jG##, ###.
                   ,####,        ;################:        i####K           ###W   G##, ###.
                  .E####E                                  E#####          E####,  G##, ###.
                  .######         ittitttttttttiti         ###### .        #####W  D##; ###.
                  E######;.       W##############E        ,######K        i####### D##; ###.
                  #######W        W##############K.       K###D###i       ########,D##; ###.
                 K###i.####       W##j        D##E       W###: #### .    W#####GL#,G##; ###.
                D###L  t###j      W##f        E##E      i###W  j###W    .##E###D f;D##; ###.
             .,W###L    t####     W##f        E##E    .W###f    .####:  .#K ###D .      ###.
             i####i      GW###;   W##GiiiiiiijW##D   i####E     .;####G .K. ###D       ,###.
            G###t.         G#W#.  W##########W###:. ,###D          tW#WE    ###D     :####W
            D#G,            :E#.  EWWWWKKKEWKWWWL   :#K,             L#D    ###G     .###W:.
                      K##E..:. .       K###                   fW#D                 D##W
            .KEKKEW,  #######D         K###          ffLfLfi  ###L:::::: iiiiiiiGED####GDDf
            .######;;W#######G         K###          ######L ;#########E #################D
            .##KD##j####:.###j         K###          ###D##L EW########E #######W##WffGG##D
            .##Dj##L##;::W###::        K###          ### ##f:###i        fjj,###K##L##j.##D
            .##Gj##;#W#########        K###,,;;;;;   ### ##LW##t  .      ##Wi#W#W##GW#K.##D
            .##Gj##;####W#####W        K##########,  ### ##G###WKKKKKKj  ###j##E###GjGG,##D
            .##Gj##;### ##W.##K        K##########;  ### ##LGW########f  ###L##DW##L .D###G
            .######;### ##W ##K        K###          ### ##L DEDEE####i  E#####f###L ,####.
            .######;### ###.##K        K###          ### ##f     D###D   f#####,###W,ititi;,
            .##Gt##;##########E        K###          ### ##L    i##WW    .##### E##########L
            .##Gj##;#########E         K###          ### ##L . t###K      E###i  ;GGGGGGW##L
            .##Dt##;.  W## .           K###          ### ##L   ####.      ;###. ..      ###L
            .##Dt##,  E####,           K###          ### ##f  D###t  .    ,###  #######GW##L
            .##Gf##; :#####,           K###          ### ##f j###L   ..   D###f #######GW##L
            .######,L###D##; ##.,ttttttW###tttttttt: ######i ###W.   ##t W##D##D        ###L
            .#####Dt###;G##Ef##.j##################t ###### t####tttf##i W#G E#W    .itt###L
             .     E#f  :###### j##################j        i##########. #i. .LW    .######
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	/**
	 * 文件上传配置
	 *
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//  单个数据大小
		factory.setMaxFileSize("1024MB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("10240MB");
		return factory.createMultipartConfig();
	}
}
