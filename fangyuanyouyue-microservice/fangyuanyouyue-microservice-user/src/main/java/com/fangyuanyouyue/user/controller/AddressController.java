package com.fangyuanyouyue.user.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.UserAddressDto;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.UserAddressInfoService;
import com.fangyuanyouyue.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/address")
@Api(description = "收货地址系统Controller")
@RefreshScope
public class AddressController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAddressInfoService userAddressInfoService;

    @ApiOperation(value = "添加收货地址", notes = "(UserAddressDto)添加收货地址",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receiverName", value = "收货人姓名",  required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receiverPhone", value = "联系电话",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "省",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "市",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "area", value = "区",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细收货地址",  required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "postCode", value = "邮编",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1默认地址 2其他",dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/addAddress")
    @ResponseBody
    public BaseResp addAddress(UserParam param) throws IOException {
        try {
            log.info("----》添加收货地址《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getReceiverName())){
                return toError("收货人不能为空！");
            }
            if(StringUtils.isEmpty(param.getReceiverPhone())){
                return toError("联系电话不能为空！");
            }
            if(StringUtils.isEmpty(param.getProvince()) || StringUtils.isEmpty(param.getCity()) || StringUtils.isEmpty(param.getArea())){
                return toError("省市区不能为空！");
            }
            if(StringUtils.isEmpty(param.getAddress())){
                return toError("详细收货地址不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
            }
            //添加收货地址
            List<UserAddressDto> userAddressDtos = userAddressInfoService.addAddress(param.getToken(),param.getReceiverName(),param.getReceiverPhone(),param.getProvince(),param.getCity(),param.getArea(),param.getAddress(),param.getPostCode(),param.getType());
            return toSuccess(userAddressDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改收货地址", notes = "(void)修改收货地址",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "收货地址ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "receiverName", value = "收货人姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receiverPhone", value = "联系电话",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "省", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "市", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "area", value = "区", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细收货地址", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateAddress")
    @ResponseBody
    public BaseResp updateAddress(UserParam param) throws IOException {
        try {
            log.info("----》修改收货地址《----");
            log.info("参数："+param.toString());
            if(param.getAddressId()==null || param.getAddressId().intValue()==0){
                return toError("收货地址ID不能为空！");
            }
            if(StringUtils.isEmpty(param.getReceiverName())){
                return toError("收货人不能为空！");
            }
            if(StringUtils.isEmpty(param.getReceiverPhone())){
                return toError("联系电话不能为空！");
            }
            if(StringUtils.isEmpty(param.getProvince()) || StringUtils.isEmpty(param.getCity()) || StringUtils.isEmpty(param.getArea())){
                return toError("省市区不能为空！");
            }
            if(StringUtils.isEmpty(param.getAddress())){
                return toError("详细收货地址不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
            }
            //修改收货地址
            userAddressInfoService.updateAddress(param.getToken(),param.getAddressId(),param.getReceiverName(),param.getReceiverPhone(),param.getProvince(),param.getCity(),param.getArea(),param.getAddress(),param.getPostCode(),param.getType());
            return toSuccess("修改收货地址成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "删除收货地址", notes = "(UserAddressDto)删除收货地址",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/deleteAddress")
    @ResponseBody
    public BaseResp deleteAddress(UserParam param) throws IOException {
        try {
            log.info("----》删除收货地址《----");
            log.info("参数："+param.toString());
            if(param.getAddressId()==null){
                return toError("收货地址ID不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
            }
            //删除收货地址
            List<UserAddressDto> userAddressDtos = userAddressInfoService.deleteAddress(param.getToken(),param.getAddressId());
            return toSuccess(userAddressDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "获取收货地址列表", notes = "(UserAddressDto)获取收货地址列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "地址id",dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getAddressList")
    @ResponseBody
    public BaseResp getAddressList(UserParam param) throws IOException {
        try {
            log.info("----》获取收货地址列表《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
            }
            //获取收货地址列表
            List<UserAddressDto> userAddressDtos = userAddressInfoService.getAddressList(param.getToken(),param.getAddressId());
            return toSuccess(userAddressDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "设置默认收货地址", notes = "(void)设置默认收货地址",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/defaultAddress")
    @ResponseBody
    public BaseResp defaultAddress(UserParam param) throws IOException {
        try {
            log.info("----》设置默认收货地址《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user = userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
            }
            //设置默认收货地址
            userAddressInfoService.defaultAddress(param.getToken(),param.getAddressId());
            return toSuccess("设置默认收货地址成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "获取默认地址", notes = "(UserAddressDto)获取默认地址",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getDefaultAddress")
    @ResponseBody
    public BaseResp getDefaultAddress(UserParam param) throws IOException {
        try {
            log.info("----》获取默认地址《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
            }
            //获取默认地址
            UserAddressDto defaultAddress = userAddressInfoService.getDefaultAddress(param.getToken());
            return toSuccess(defaultAddress);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
