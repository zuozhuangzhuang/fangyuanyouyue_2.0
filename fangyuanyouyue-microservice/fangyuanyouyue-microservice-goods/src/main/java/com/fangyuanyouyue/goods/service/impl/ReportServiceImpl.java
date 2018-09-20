package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.Score;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.dao.ReportMapper;
import com.fangyuanyouyue.goods.dto.adminDto.AdminReportGoodsDto;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.Report;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;
import com.fangyuanyouyue.goods.service.ReportService;
import com.fangyuanyouyue.goods.service.SchedualMessageService;
import com.fangyuanyouyue.goods.service.SchedualWalletService;
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
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private SchedualWalletService schedualWalletService;

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
            report.setStatus(Status.NO.getValue());
            reportMapper.insert(report);
        }
    }

    @Override
    public void dealReport(Integer id, String content) throws ServiceException {
        //1、删除商品 2、发送信息（content）
        Report report = reportMapper.selectByPrimaryKey(id);
        if(report == null){
            throw new ServiceException("未找到举报信息！");
        }
        if(report.getType() == 1){
            //删除商品
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(report.getBusinessId());
            if(goodsInfo == null){
                throw new ServiceException("未找到商品、抢购");
            }
            goodsInfo.setStatus(5);
            goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
            //举报者+20
            schedualWalletService.updateCredit(report.getUserId(), Credit.REPORT_VERIFY.getCredit(),Status.ADD.getValue());
            //被举报-40
            schedualWalletService.updateCredit(goodsInfo.getUserId(), Credit.REPORT_VERIFYED.getCredit(),Status.SUB.getValue());
            //很抱歉，您的商品/抢购【名称】被多用户举报，并经官方核实。已被删除，删除理由：￥@……#%￥&#%￥……@。点击查看详情
            schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                    "很抱歉，您的"+(goodsInfo.getType().intValue()==Status.GOODS.getValue()?"商品【":"抢购【")+goodsInfo.getName()+"】被多用户举报，并经官方核实。已被删除，删除理由："+content+"。点击查看详情",
                    Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_GOODS.getMessage(),goodsInfo.getId().toString());
        }
        report.setStatus(Status.YES.getValue());
        reportMapper.updateByPrimaryKey(report);
    }

    @Override
    public Pager getGoodsReportPage(AdminGoodsParam param) throws ServiceException {
        Integer total = reportMapper.countPage(param.getType(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
        List<Report> list = reportMapper.getGoodsReportPage(param.getType(),param.getStart()*param.getLimit(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        ArrayList<AdminReportGoodsDto> datas = AdminReportGoodsDto.toDtoList(list);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }
}
