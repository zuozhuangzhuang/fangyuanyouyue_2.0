package com.fangyuanyouyue.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.CommentService;
import com.fangyuanyouyue.goods.service.GoodsInfoService;
import com.fangyuanyouyue.goods.service.SchedualRedisService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/comment")
@Api(description = "评论系统Controller")
@RefreshScope
public class CommentController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SchedualRedisService schedualRedisService;

    @ApiOperation(value = "发布评论/回复", notes = "(void)发布评论/回复",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品id",required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "commentId", value = "回复评论id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "评论内容",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "img1Url", value = "图片地址1", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "img2Url", value = "图片地址2", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "img3Url", value = "图片地址3", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/addComment")
    @ResponseBody
    public BaseResp addComment(GoodsParam param) throws IOException {
        try {
            log.info("----》发布评论/回复《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            if(param.getGoodsId() == null){
                return toError("商品id不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getImg1Url() == null && StringUtils.isEmpty(param.getContent())){
                return toError("内容不能为空！");
            }
            param.setUserId(userId);

            Integer commentId = commentService.addComment(param);
            return toSuccess(commentId);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "评论点赞(取消点赞)", notes = "(void)评论点赞",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "commentId", value = "回复评论id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1点赞 2取消点赞",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/commentLikes")
    @ResponseBody
    public BaseResp commentLikes(GoodsParam param) throws IOException {
        try {
            log.info("----》评论点赞《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getCommentId() == null){
                return toError("评论id不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            commentService.commentLikes(userId,param.getCommentId(),param.getType());
            if(param.getType().intValue() == 1){
                return toSuccess("评论点赞成功！");
            }else if(param.getType().intValue() == 2){
                return toSuccess("取消点赞成功！");
            }else{
                return toError("类型异常！");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "查看全部评论", notes = "(GoodsCommentDto)查看全部评论",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getComments")
    @ResponseBody
    public BaseResp getComments(GoodsParam param) throws IOException {
        try {
            log.info("----》查看全部评论《----");
            log.info("参数："+param.toString());
            Integer userId = null;
            if(StringUtils.isNotEmpty(param.getToken())){
                //根据用户token获取userId
                userId = (Integer)schedualRedisService.get(param.getToken());
            }
            if(param.getGoodsId() == null){
                return toError("商品id不能为空！");
            }
            if(param.getStart() == null || param.getStart().intValue() < 0 || param.getLimit() == null || param.getLimit() < 1){
                return toError("分页参数异常！");
            }
            List<GoodsCommentDto> comments = commentService.getComments(userId,param.getGoodsId(),param.getStart(),param.getLimit());
            return toSuccess(comments);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "（商品/抢购）我的评论", notes = "(GoodsCommentDto)查看（商品/抢购）我的评论",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1商品 2抢购",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/myComments")
    @ResponseBody
    public BaseResp myComments(GoodsParam param) throws IOException {
        try {
            log.info("----》（商品/抢购）我的评论《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            if(param.getStart() == null || param.getStart().intValue() < 0 || param.getLimit() == null || param.getLimit() < 1){
                return toError("分页参数异常！");
            }
            //（商品/抢购）我的评论
            List<GoodsCommentDto> myComments = commentService.myComments(userId,param.getType(),param.getStart(),param.getLimit());
            return toSuccess(myComments);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "删除评论", notes = "(void)删除评论",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "commentIds", value = "评论id数组",allowMultiple = true,required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/deleteComment")
    @ResponseBody
    public BaseResp deleteComment(GoodsParam param) throws IOException {
        try {
            log.info("----》删除评论《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getCommentIds() == null || param.getCommentIds().length < 1){
                return toError("评论id不能为空！");
            }
            commentService.deleteComment(param.getCommentIds());
            return toSuccess("删除评论成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
