package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.BannerIndexMapper;
import com.fangyuanyouyue.goods.dto.BannerIndexDto;
import com.fangyuanyouyue.goods.dto.adminDto.AdminBannerDto;
import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.BannerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "bannerService")
public class BannerServiceImpl implements BannerService{

    @Autowired
    private BannerIndexMapper bannerIndexMapper;

    @Override
    public void addBanner(AdminGoodsParam param) throws ServiceException {
        BannerIndex bannerIndex = new BannerIndex();
        bannerIndex.setBusinessId(param.getBusinessId());
        bannerIndex.setJumpType(param.getJumpType());
        bannerIndex.setBusinessType(param.getBusinessType());
        bannerIndex.setType(param.getType());
        bannerIndex.setTitle(param.getTitle());
        bannerIndex.setImgUrl(param.getImgUrl());
        bannerIndex.setSort(param.getSort());
        bannerIndex.setStatus(1);//是否展示，1展示 2不展示
        bannerIndex.setAddTime(DateStampUtils.getTimesteamp());
        bannerIndexMapper.insert(bannerIndex);
    }

    @Override
    public void deleteBanner(Integer bannerId) throws ServiceException {
        bannerIndexMapper.deleteByPrimaryKey(bannerId);
    }

    @Override
    public void updateBanner(AdminGoodsParam param) throws ServiceException {
        BannerIndex bannerIndex = bannerIndexMapper.selectByPrimaryKey(param.getId());
        if(bannerIndex == null){
            throw new ServiceException("获取轮播图失败！");
        }else{
            if(param.getType() != null){
                bannerIndex.setType(param.getType());
            }
            if(StringUtils.isNotEmpty(param.getImgUrl())){
                bannerIndex.setImgUrl(param.getImgUrl());
            }
            if(param.getJumpType() != null){
                bannerIndex.setJumpType(param.getJumpType());
            }
            bannerIndex.setBusinessType(param.getBusinessType());
            bannerIndex.setBusinessId(param.getBusinessId());
            if(StringUtils.isNotEmpty(param.getTitle())){
                bannerIndex.setTitle(param.getTitle());
            }
            if(param.getSort() != null){
                bannerIndex.setSort(param.getSort());
            }
            if(param.getStatus() != null){
                bannerIndex.setStatus(param.getStatus());
            }
            bannerIndexMapper.updateByPrimaryKeySelective(bannerIndex);
        }
    }

    @Override
    public Pager bannerList(AdminGoodsParam param) throws ServiceException {
        Integer total = bannerIndexMapper.countPage(param.getType(),param.getJumpType(),param.getBusinessType(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<BannerIndex> banners = bannerIndexMapper.getPage(param.getType(),param.getJumpType(),param.getBusinessType(),param.getStart(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());

        List<AdminBannerDto> bannerIndexDtos = AdminBannerDto.toDtoList(banners);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(bannerIndexDtos);
        return pager;
    }



}
