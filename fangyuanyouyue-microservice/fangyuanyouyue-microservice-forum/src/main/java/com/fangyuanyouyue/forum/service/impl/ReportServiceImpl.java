package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dao.ReportMapper;
import com.fangyuanyouyue.forum.dto.admin.AdminReportDto;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.model.Report;
import com.fangyuanyouyue.forum.param.AdminForumParam;
import com.fangyuanyouyue.forum.service.ReportService;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "reportService")
public class ReportServiceImpl implements ReportService{
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ForumInfoMapper forumInfoMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private AppraisalDetailMapper appraisalDetailMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;


    @Override
    public Pager getForumReportPage(AdminForumParam param) throws ServiceException {
        Integer total = reportMapper.countForumPage(param.getType(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
        List<Report> list = reportMapper.getForumReportPage(param.getType(),param.getStart(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        ArrayList<AdminReportDto> datas = AdminReportDto.toDtoList(list);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }


    @Override
    public Pager getAppraisalReportPage(AdminForumParam param) throws ServiceException {
        Integer total = reportMapper.countAppraisalPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
        List<Report> list = reportMapper.getAppraisalReportPage(param.getStart(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        ArrayList<AdminReportDto> datas = AdminReportDto.toDtoList(list);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }


    @Override
    @Transactional
    @TxTransaction(isStart=true)
    public void dealReport(Integer id, String content) throws ServiceException {
        //1、删除帖子 2、发信息给用户 3、扣除被举报用户信誉度 4、给举报用户增加信誉度
        Report report = reportMapper.selectByPrimaryKey(id);
        if(report == null){
            throw new ServiceException("未找到举报信息！");
        }
        //举报者+20
        String result = schedualWalletService.updateCredit(report.getUserId(), Credit.REPORT_VERIFY.getCredit(), Status.ADD.getValue());
        BaseResp br = ParseReturnValue.getParseReturnValue(result);
        if(!br.getCode().equals(ReCode.SUCCESS)){
            throw new ServiceException(br.getCode(),br.getReport());
        }
        if(report.getType() == 2 || report.getType() == 3){
            ForumInfo forumInfo = forumInfoMapper.selectByPrimaryKey(report.getBusinessId());
            //删除帖子、视频
            forumInfo.setStatus(Status.HIDE.getValue());
            forumInfoMapper.updateByPrimaryKey(forumInfo);
            //被举报-40
            String forumUserResult = schedualWalletService.updateCredit(forumInfo.getUserId(), Credit.REPORT_VERIFYED.getCredit(),Status.SUB.getValue());
            BaseResp forumUserBr = ParseReturnValue.getParseReturnValue(forumUserResult);
            if(!forumUserBr.getCode().equals(ReCode.SUCCESS)){
                throw new ServiceException(forumUserBr.getCode(),forumUserBr.getReport());
            }
            //很抱歉，您的帖子/视频/全民鉴定【名称】被多用户举报，并经官方核实。已被删除，删除理由：￥@……#%￥&#%￥……@
            schedualMessageService.easemobMessage(forumInfo.getUserId().toString(),
                    "很抱歉，您的"+(forumInfo.getType()==1?"帖子【":"视频【")+forumInfo.getTitle()+"】被多用户举报，并经官方核实。已被删除，删除理由："+content+"",
                    Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
        }else if(report.getType() == 4){
            AppraisalDetail detail = appraisalDetailMapper.selectByPrimaryKey(report.getBusinessId());
            //删除全民鉴定
            detail.setStatus(Status.DELETE.getValue());
            if(detail.getBonus() != null){
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(detail.getUserId(),detail.getBonus(),Status.ADD.getValue()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS)){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                //余额账单
                //订单号
                final IdGenerator idg = IdGenerator.INSTANCE;
                String orderNo = idg.nextId();
                schedualWalletService.addUserBalanceDetail(detail.getUserId(),detail.getBonus(), Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),orderNo,"【"+detail.getTitle()+"】官方删除",detail.getUserId(),null,Status.APPRAISAL.getValue(),orderNo);
            }
            appraisalDetailMapper.updateByPrimaryKey(detail);
            //被举报-40
            String appraisalUserResult = schedualWalletService.updateCredit(detail.getUserId(), Credit.REPORT_VERIFYED.getCredit(),Status.SUB.getValue());
            BaseResp appraisalUserBr = ParseReturnValue.getParseReturnValue(appraisalUserResult);
            if(!appraisalUserBr.getCode().equals(ReCode.SUCCESS)){
                throw new ServiceException(appraisalUserBr.getCode(),appraisalUserBr.getReport());
            }
            //很抱歉，您的帖子/视频/全民鉴定【名称】被多用户举报，并经官方核实。已被删除，删除理由：￥@……#%￥&#%￥……@
            schedualMessageService.easemobMessage(detail.getUserId().toString(),
                    "很抱歉，您的全民鉴定【"+detail.getTitle()+"】被多用户举报，并经官方核实。已被删除，删除理由："+content, Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
        }
        report.setStatus(Status.YES.getValue());
        reportMapper.updateByPrimaryKeySelective(report);
    }
}
