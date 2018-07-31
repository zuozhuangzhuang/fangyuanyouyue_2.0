package com.fangyuanyouyue.forum.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.BannerIndexMapper;
import com.fangyuanyouyue.forum.dto.BannerIndexDto;
import com.fangyuanyouyue.forum.model.BannerIndex;
import com.fangyuanyouyue.forum.service.BannerIndexService;


@Service(value = "bannerIndexService")
public class BannerIndexServiceImp implements BannerIndexService {

    @Autowired
    private BannerIndexMapper bannerIndexMapper;


	@Override
	public List<BannerIndexDto> getBannerIndex() throws ServiceException {
		List<BannerIndex> list = bannerIndexMapper.selectAll(StatusEnum.STATUS_NORMAL.getValue());
		return BannerIndexDto.toDtoList(list);
	}
    

}
