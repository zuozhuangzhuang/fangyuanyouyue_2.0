package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.ReportMapper;
import com.fangyuanyouyue.goods.model.Report;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;
import com.fangyuanyouyue.goods.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "reportService")
@Transactional(rollbackFor=Exception.class)
public class ReportServiceImpl implements ReportService{
    @Autowired
    private ReportMapper reportMapper;
    @Override
    public void report(Integer userId, Integer businessId, String reason, Integer type) throws ServiceException {
        Report report = reportMapper.selectByUserIdBusinessId(userId, businessId, type);
        if(report != null){
            throw new ServiceException("已举报过！");
        }else{
            report = new Report();
            report.setBusinessId(businessId);
            report.setReason(reason);
            report.setType(type);
            report.setUserId(userId);
            report.setAddTime(DateStampUtils.getTimesteamp());
            reportMapper.insert(report);
        }
    }

    @Override
    public void dealReport(Integer id, String content, Integer status) throws ServiceException {
        //TODO 1、根据status处理举报信息或商品 2、根据status给举报人或被举报人发送信息（content）

    }

    @Override
    public Pager getReportPage(AdminGoodsParam param) throws ServiceException {
        Integer total = reportMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
        List<Report> list = reportMapper.getReportPage(param.getType(),param.getStart()*param.getLimit(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(list);
        return pager;
    }
}
