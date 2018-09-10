package com.fangyuanyouyue.wallet.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dao.PointGoodsMapper;
import com.fangyuanyouyue.wallet.dto.PointGoodsDto;
import com.fangyuanyouyue.wallet.model.PointGoods;
import com.fangyuanyouyue.wallet.service.PointGoodsService;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "pointGoodsService")
@Transactional(rollbackFor=Exception.class)
public class PointGoodsServiceImpl implements PointGoodsService {

    @Autowired
    private PointGoodsMapper pointGoodsMapper;

    @Override
    public List<PointGoodsDto> getPointGoods(Integer start, Integer limit) throws ServiceException {
        List<PointGoods> list = pointGoodsMapper.selectList(start, limit);
        return PointGoodsDto.toDtoList(list);
    }

    public static void main(String[] args) {
        //n辆车
        //每辆车的油量
        //总距离
        BigDecimal n = new BigDecimal(50);
        BigDecimal l = new BigDecimal(50);
        BigDecimal s = new BigDecimal(0);
        for (BigDecimal i = new BigDecimal(0); i.compareTo(n) < 0; i = i.add(new BigDecimal(1))) {
            s = s.add(l.divide(n.subtract(i), 5, RoundingMode.HALF_UP));
            if (i.compareTo(n.subtract(new BigDecimal(1))) == 0) {
                System.out.println("最后一辆车,这次跑了：" + l.divide(n.subtract(i), 5, RoundingMode.HALF_UP) + "公里");
            } else {
                System.out.println("第" + (i.intValue() + 1) + "次换油，这次跑了：" + l.divide(n.subtract(i), 5, RoundingMode.HALF_UP) + "公里");
                System.out.println("还剩下" + n.subtract(new BigDecimal(1)).subtract(i) + "辆车");
            }
            System.out.println("目前跑了" + s + "公里");
            System.out.println("--------------------------------------------------");
        }
        System.out.println("共跑了" + s + "公里");
    }
}
