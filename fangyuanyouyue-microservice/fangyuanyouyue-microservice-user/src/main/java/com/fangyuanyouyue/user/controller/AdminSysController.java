package com.fangyuanyouyue.user.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.admin.AdminDailyStatisticsDto;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;
import com.fangyuanyouyue.user.dto.admin.AdminOperatorDto;
import com.fangyuanyouyue.user.dto.admin.AdminProcessDto;
import com.fangyuanyouyue.user.dto.admin.AdminRoleDto;
import com.fangyuanyouyue.user.param.AdminMenuParam;
import com.fangyuanyouyue.user.param.AdminRoleParam;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.SysMenuService;
import com.fangyuanyouyue.user.service.SysOperatorService;
import com.fangyuanyouyue.user.service.SysRoleService;
import com.fangyuanyouyue.user.service.SystemService;
import com.fangyuanyouyue.user.service.UserInfoService;
import com.fangyuanyouyue.user.service.VersionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "用户后台管理系统Controller")
@Controller
@RequestMapping(value = "/system")
@RefreshScope
public class AdminSysController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysOperatorService sysOperatorService;



    @ApiOperation(value = "登录", notes = "用户登录",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "登录密码",  required = true,dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/login")
    @ResponseBody
    public BaseResp delete(String loginCode,String password) throws IOException {
        try {
            log.info("后台后台登录 - "+loginCode);

            //测试登录
            if("admin".equals(loginCode) && "123456".equals(password)) {
                HashMap map = new HashMap<String,String>();
                map.put("token", "abcdefg");
                return toSuccess(map);
            }else {
                return toError("账号或密码有误");
            }

        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "版本更新列表", notes = "版本更新列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/versionList")
    @ResponseBody
    public BaseResp versionList(AdminUserParam param) throws IOException {
        try {
            log.info("----》版本更新列表《----");
            log.info("参数："+param.toString());
            Pager pager = versionService.getVersionList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "新增版本", notes = "新增版本",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "versionNo", value = "版本号", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionDesc", value = "版本描述", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionName", value = "版本名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "apk文件", required = true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "packageName", value = "安卓包名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通更新 2强制更新", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/versionAdd")
    @ResponseBody
    public BaseResp versionAdd(AdminUserParam param) throws IOException {
        try {
            log.info("----》新增版本《----");
            log.info("参数："+param.toString());
            if(param.getVersionNo() == null){
                return toError("版本号不能为空！");
            }
            if(StringUtils.isEmpty(param.getVersionDesc())){
                return toError("版本描述不能为空！");
            }
            if(StringUtils.isEmpty(param.getVersionName())){
                return toError("版本名称不能为空！");
            }
            if(param.getFile() == null){
                return toError("apk文件不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            versionService.versionAdd(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "修改版本信息", notes = "修改版本信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本文件id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionNo", value = "版本号", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionDesc", value = "版本描述", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionName", value = "版本名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "apk文件", required = false, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "packageName", value = "安卓包名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通更新 2强制更新", required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/versionModify")
    @ResponseBody
    public BaseResp versionModify(AdminUserParam param) throws IOException {
        try {
            log.info("----》修改版本信息《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("版本文件id不能为空！");
            }
            versionService.versionModify(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "删除版本", notes = "删除版本",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本文件id", required = true, dataType = "int", paramType = "query")
    })
    @DeleteMapping(value = "/versionDelete")
    @ResponseBody
    public BaseResp versionDelete(AdminUserParam param) throws IOException {
        try {
            log.info("----》删除版本《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("版本文件id不能为空！");
            }
            versionService.versionDelete(param.getId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "发送系统消息", notes = "发送系统消息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "消息体", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/sendMessage")
    @ResponseBody
    public BaseResp sendMessage(AdminUserParam param) throws IOException {
        try {
            log.info("----》发送系统消息《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getContent())){
                return toError("消息体不能为空！");
            }
            systemService.sendMessage(param.getContent());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "获取统计信息", notes = "(AdminProcessDto)获取统计信息",response = BaseResp.class)
    @PostMapping(value = "/getProcess")
    @ResponseBody
    public BaseResp getProcess() throws IOException {
        try {
            log.info("----》获取统计信息《----");
            AdminProcessDto process = systemService.getProcess();
            return toSuccess(process);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "系统消息历史列表", notes = "系统消息历史列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/sysMsgList")
    @ResponseBody
    public BaseResp sysMsgList(AdminUserParam param) throws IOException {
        try {
            log.info("----》系统消息历史列表《----");
            log.info("参数："+param.toString());
            Pager pager = systemService.sysMsgList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "获取统计列表", notes = "(AdminProcessDto)获取统计列表",response = BaseResp.class) @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "天数", required = true, dataType = "int", paramType = "query"),
    })
    @PostMapping(value = "/getProcessList")
    @ResponseBody
    public BaseResp getProcessList(AdminUserParam param) throws IOException {
        try {
            log.info("----》获取统计列表《----");
            log.info("参数："+param.toString());
            if(param.getCount() == null){
                return toError("天数不能为空！");
            }
            List<AdminDailyStatisticsDto> processList = systemService.getProcessList(param.getCount());
            return toSuccess(processList);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }


    @ApiOperation(value = "菜单列表", notes = "菜单列表",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @GetMapping(value = "/menuList")
    @ResponseBody
    public BaseResp menuList(AdminMenuParam param) throws IOException {
        try {
            log.info("----》获取全部菜单列表《----");
            log.info("参数："+param.toString());
            List<AdminMenuDto> menus = null;
            if(param.getRoleId()!=null&&param.getRoleId()>0) {
                menus = sysMenuService.getMenuByRole(param.getRoleId());
            }else {

            	menus = sysMenuService.getAllMenu();
            }
            
            
            return toSuccess(menus);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    
    @ApiOperation(value = "菜单列表", notes = "菜单列表",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @GetMapping(value = "/roleMenuList")
    @ResponseBody
    public Object menuListRole(AdminMenuParam param) throws IOException {
        try {
            log.info("----》获取角色菜单列表《----");
            log.info("参数："+param.toString());
            List<AdminMenuDto> menus = sysMenuService.getMenuByRole(param.getRoleId());
            
            return menus;
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }




    @ApiOperation(value = "添加菜单", notes = "添加菜单",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @PostMapping(value = "/menuSave")
    @ResponseBody
    public BaseResp menuAdd(AdminMenuParam param) throws IOException {
        try {
            log.info("----》添加菜单《----");
            log.info("参数："+param.toString());
            
            sysMenuService.saveMenu(param);
            
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }


    @ApiOperation(value = "删除菜单", notes = "删除菜单",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @PostMapping(value = "/menuDelete")
    @ResponseBody
    public BaseResp menuDelete(AdminUserParam param) throws IOException {
        try {
            log.info("----》删除菜单《----");
            log.info("参数："+param.toString());
            
            sysMenuService.deleteMenu(param.getId());
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }
    
    

    @ApiOperation(value = "	角色列表", notes = "角色列表",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @GetMapping(value = "/roleList")
    @ResponseBody
    public BaseResp roleList(AdminRoleParam param) throws IOException {
        try {
            log.info("----》获取全部菜单列表《----");
            log.info("参数："+param.toString());
            List<AdminRoleDto> roles = null;
            if(param.getMenuId()!=null) {
                roles = sysRoleService.getRoleByMenuId(param.getMenuId());
            }else if(param.getUserId()!=null) {
                roles = sysRoleService.getRoleByMenuId(param.getMenuId());
            }else {
            	roles = sysRoleService.getAllRole();
            }
            return toSuccess(roles);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }




    @ApiOperation(value = "添加角色", notes = "添加角色",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @PostMapping(value = "/roleSave")
    @ResponseBody
    public BaseResp roleAdd(AdminRoleParam param) throws IOException {
        try {
            log.info("----》添加角色《----");
            log.info("参数："+param.toString());
            
            sysRoleService.saveRole(param);
            
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }


    @ApiOperation(value = "删除角色", notes = "删除角色",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @PostMapping(value = "/roleDelete")
    @ResponseBody
    public BaseResp roleDelete(AdminUserParam param) throws IOException {
        try {
            log.info("----》删除角色《----");
            log.info("参数："+param.toString());
            
            sysRoleService.deleteRole(param.getId());
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }
    
    


    @ApiOperation(value = "操作员列表", notes = "操作员列表",response = BaseResp.class)
    @ApiImplicitParams({
    })
    @GetMapping(value = "/operatorList")
    @ResponseBody
    public Object operatorList(AdminRoleParam param) throws IOException {
        try {
            log.info("----》获取全部菜单列表《----");
            log.info("参数："+param.toString());
            
            List<AdminOperatorDto> datas = sysOperatorService.getAllOperator();
            HashMap<String,Object> returns = new HashMap<String,Object>();
            returns.put("success", true);
            returns.put("data",datas);
    		
            return returns;
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }


    
    
}
