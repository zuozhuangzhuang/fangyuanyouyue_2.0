package com.fangyuanyouyue.wallet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dao.PointGoodsMapper;
import com.fangyuanyouyue.wallet.dto.PointGoodsDto;
import com.fangyuanyouyue.wallet.model.PointGoods;
import com.fangyuanyouyue.wallet.service.PointGoodsService;

@Service(value = "pointGoodsService")
public class PointGoodsServiceImp implements PointGoodsService{

    @Autowired
    private PointGoodsMapper pointGoodsMapper;
    
	@Override
	public List<PointGoodsDto> getPointGoods(Integer start, Integer limit) throws ServiceException {
		List<PointGoods> list = pointGoodsMapper.selectList(start, limit);
		return PointGoodsDto.toDtoList(list);
	}

}
