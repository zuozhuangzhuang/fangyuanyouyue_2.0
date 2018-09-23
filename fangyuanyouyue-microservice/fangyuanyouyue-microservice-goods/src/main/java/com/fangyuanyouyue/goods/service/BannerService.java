package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.adminDto.AdminBannerDto;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;

import java.util.List;

public interface BannerService {
    /**
     * 新增首页轮播图
     * @param param
     * @return
     * @throws ServiceException
     */
    AdminBannerDto addBanner(AdminGoodsParam param) throws ServiceException;

    /**
     * 删除轮播图
     * @param bannerId
     * @throws ServiceException
     */
    void deleteBanner(Integer bannerId) throws ServiceException;

    /**
     * 修改首页轮播图
     * @param param
     * @return
     * @throws ServiceException
     */
    void updateBanner(AdminGoodsParam param) throws ServiceException;

    /**
     * 查看首页轮播图列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager bannerList(AdminGoodsParam param) throws ServiceException;



}
