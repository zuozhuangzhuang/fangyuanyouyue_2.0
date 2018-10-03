package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.wallet.dao.PlatformFinanceDetailMapper;
import com.fangyuanyouyue.wallet.dto.admin.AdminPlatformFinanceDetailDto;
import com.fangyuanyouyue.wallet.model.PlatformFinanceDetail;
import com.fangyuanyouyue.wallet.param.AdminWalletParam;
import com.fangyuanyouyue.wallet.service.PlatformFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service(value = "platformFinanceService")
@Transactional(rollbackFor=Exception.class)
public class PlatformFinanceServiceImpl implements PlatformFinanceService {
    @Autowired
    private PlatformFinanceDetailMapper platformFinanceDetailMapper;

    @Override
    public Pager platformFinance(AdminWalletParam param) throws ServiceException {

        Integer total = platformFinanceDetailMapper.countPage(param.getPayType(),param.getOrderType(),param.getType(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<PlatformFinanceDetail> details = platformFinanceDetailMapper.getPage(param.getPayType(),param.getOrderType(),param.getType(),param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        List<AdminPlatformFinanceDetailDto> datas = AdminPlatformFinanceDetailDto.toDtoList(details);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }

    @Override
    public void saveFinace(Integer userId, BigDecimal amount, Integer payType, Integer type, String orderNo, String title,Integer orderType,Integer sellerId,Integer buyerId,String payNo) throws ServiceException {
        PlatformFinanceDetail financeDetail = new PlatformFinanceDetail();
        financeDetail.setUserId(userId);
        financeDetail.setAmount(amount);
        financeDetail.setPayType(payType);
        if(type.equals(Status.EXPEND.getValue())){
            if(payType.equals(Status.PAY_TYPE_BALANCE.getValue())){
                financeDetail.setType(Status.EXPEND.getValue());
            }else{
                financeDetail.setType(Status.INCOME.getValue());
            }
        }else{
            financeDetail.setType(Status.EXPEND.getValue());
        }
        financeDetail.setAddTime(DateStampUtils.getTimesteamp());
        financeDetail.setTitle(title);
        financeDetail.setOrderNo(orderNo);
        financeDetail.setOrderType(orderType);
        financeDetail.setSellerId(sellerId);
        financeDetail.setBuyerId(buyerId);
        financeDetail.setPayNo(payNo);
        platformFinanceDetailMapper.insert(financeDetail);
    }


}
