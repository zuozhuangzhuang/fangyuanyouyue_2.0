package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
import com.fangyuanyouyue.forum.dto.MyColumnDto;
import com.fangyuanyouyue.forum.model.ForumColumnApply;

/**
 * 专栏接口
 * @author wuzhimin
 *
 */
public interface ForumColumnService {

	/**
	 * 获取全部专栏
	 * @param typeId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnTypeDto> getColumnList(Integer typeId,Integer start,Integer limit) throws ServiceException;

	/**
	 * 获取精选专栏
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnDto> getChosenColumnList(Integer start,Integer limit) throws ServiceException;

	/**
	 * 获取专栏分类列表
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnTypeDto> getForumTypeList() throws ServiceException;

	/**
	 * 发布专栏
	 * @param userId
	 * @param typeId
	 * @param name
	 * @param payType
	 * @param payPwd
	 * @throws ServiceException
	 */
	Object addColumn(Integer userId,Integer typeId,String name,Integer payType,String payPwd) throws ServiceException;

	/**
	 * 处理专栏申请
	 * @param applyId
	 * @param status
	 * @param coverImgUrl
	 * @param reason
	 * @throws ServiceException
	 */
	void handle(Integer applyId,Integer status,String coverImgUrl,String reason) throws ServiceException;

	/**
	 * 专栏申请列表
	 * @param start
	 * @param limit
	 * @param keyword
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	List<ForumColumnApply> applyList(Integer start, Integer limit, String keyword,Integer status) throws ServiceException;

	/**
	 * 生成申请记录
	 * @param orderNo
	 * @param thirdOrderNo
	 * @param payType
	 * @return
	 * @throws ServiceException
	 */
	boolean applyColumn(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;

	/**
	 * 我是栏主
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	MyColumnDto myColumn(Integer userId,Integer start,Integer limit) throws ServiceException;
	

    /**
     * 获取后台分页数据
     * @param param
     * @return
     */
    Pager getPage(BasePageReq param);
    

    /**
     * 获取后台分页数据
     * @param param
     * @return
     */
    Pager getPageApply(BasePageReq param);

	/**
	 * 修改专栏
	 * @param columnId
	 * @param name
	 * @param coverImgUrl
	 * @param isChosen
	 * @throws ServiceException
	 */
    void updateColumn(Integer columnId,String name,String coverImgUrl,Integer isChosen) throws ServiceException;
    
}
