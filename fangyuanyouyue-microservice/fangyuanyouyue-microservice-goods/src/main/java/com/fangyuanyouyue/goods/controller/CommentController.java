package com.fangyuanyouyue.goods.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.ResultUtil;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.CommentService;
import com.fangyuanyouyue.goods.service.GoodsInfoService;
import com.fangyuanyouyue.goods.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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

    @ApiOperation(value = "发布评论/回复", notes = "(void)发布评论/回复",response = ResultUtil.class)
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
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            if(param.getGoodsId() == null){
                return toError(ReCode.FAILD.getValue(),"商品id不能为空！");
            }
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            if(param.getImg1Url() == null && StringUtils.isEmpty(param.getContent())){
                return toError(ReCode.FAILD.getValue(),"内容不能为空！");
            }
            param.setUserId(userId);

            commentService.addComment(param);
            return toSuccess("发布评论/回复成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "评论点赞", notes = "(void)评论点赞",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "commentId", value = "回复评论id",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/commentLikes")
    @ResponseBody
    public BaseResp commentLikes(GoodsParam param) throws IOException {
        try {
            log.info("----》评论点赞《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getCommentId() == null){
                return toError(ReCode.FAILD.getValue(),"评论id不能为空！");
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);

            commentService.commentLikes(param.getCommentId());
            return toSuccess("评论点赞成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "查看全部评论", notes = "(GoodsCommentDto)查看全部评论",response = ResultUtil.class)
    @ApiImplicitParams({
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
            if(param.getGoodsId() == null){
                return toError(ReCode.FAILD.getValue(),"商品id不能为空！");
            }
            if(param.getStart() == null || param.getLimit() == null){
                return toError(ReCode.FAILD.getValue(),"分页参数不能为空！");
            }
            List<GoodsCommentDto> comments = commentService.getComments(param.getGoodsId(),param.getStart(),param.getLimit());
            return toSuccess(comments);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

}
