package com.fangyuanyouyue.wallet.controller;

import io.swagger.annotations.Api;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/adminWallet")
@Api(description = "后台管理系统Controller")
@RefreshScope
public class AdminController {
    //TODO 查看平台收支
    //TODO 查看用户收支
    //TODO 提现申请管理
    //TODO 冻结用户余额
    //TODO 修改用户扩展信息
}
