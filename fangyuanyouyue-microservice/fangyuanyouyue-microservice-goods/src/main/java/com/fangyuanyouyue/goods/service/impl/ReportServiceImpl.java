package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.ReportMapper;
import com.fangyuanyouyue.goods.model.Report;
import com.fangyuanyouyue.goods.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "reportService")
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
}
