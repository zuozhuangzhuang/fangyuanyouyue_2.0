package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.AppraisalDetailDto;
import com.fangyuanyouyue.goods.dto.AppraisalOrderInfoDto;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;

import java.util.List;

/**
 * 官方鉴定
 */
public interface AppraisalService {

    /**
     * 申请鉴定
     * @param userId
     * @param goodsIds
     * @param title
     * @param description
     * @param imgUrls
     * @param videoUrl
     * @param videoImg
     * @return
     * @throws ServiceException
     */
    AppraisalOrderInfoDto addAppraisal(Integer userId, Integer[] goodsIds, String title, String description, String[] imgUrls, String videoUrl,String videoImg) throws ServiceException;

    /**
     * 取消鉴定，删除鉴定订单
     * @param userId
     * @param orderId
     * @throws ServiceException
     */
    void cancelAppraisal(Integer userId,Integer orderId) throws ServiceException;

    /**
     * 鉴定查询
     * @param userId
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<AppraisalDetailDto> getAppraisal(Integer userId,Integer start,Integer limit) throws ServiceException;

    /**
     * 鉴定结果
     * @param userId
     * @param detailId
     * @return
     * @throws ServiceException
     */
    AppraisalDetailDto appraisalDetail(Integer userId,Integer detailId) throws ServiceException;

    /**
     * 鉴定支付
     * @param userId
     * @param orderId
     * @param type
     * @param payPwd
     * @return
     * @throws ServiceException
     */
    Object payAppraisal(Integer userId,Integer orderId,Integer type,String payPwd) throws ServiceException;

    /**
     * 鉴定展示
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<AppraisalDetailDto> getAllAppraisal(Integer start,Integer limit) throws ServiceException;

    /**
     * 修改订单状态
     * @param orderNo
     * @param thirdOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;

    /**
     * 官方鉴定列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager appraisalList(AdminGoodsParam param) throws ServiceException;

    /**
     * 后台处理官方鉴定
     * @param id
     * @param opinion
     * @param status
     * @param isShow
     * @throws ServiceException
     */
    void updateAppraisal(Integer id,Integer status,String opinion,Integer isShow) throws ServiceException;

    /**
     * 删除官方鉴定
     * @param userId
     * @param ids
     * @throws ServiceException
     */
    void deleteAppraisal(Integer userId,Integer[] ids) throws ServiceException;
}
