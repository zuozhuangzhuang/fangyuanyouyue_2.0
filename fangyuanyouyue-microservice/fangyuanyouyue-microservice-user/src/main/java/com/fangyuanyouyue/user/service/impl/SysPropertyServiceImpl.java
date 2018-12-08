package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.SysPropertyMapper;
import com.fangyuanyouyue.user.dto.SysPropertyDto;
import com.fangyuanyouyue.user.dto.admin.AdminSysPropertyDto;
import com.fangyuanyouyue.user.model.SysProperty;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.SysPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "ruleService")
public class SysPropertyServiceImpl implements SysPropertyService {
    @Autowired
    private SysPropertyMapper sysPropertyMapper;

    @Override
    public SysPropertyDto getInviteRule() throws ServiceException {
        SysProperty sysProperty = sysPropertyMapper.getRuleByKey(Status.INVITE_RULE.getMessage());
        if(sysProperty == null){
            throw new ServiceException("规则标识码错误！");
        }
        SysPropertyDto ruleDto = new SysPropertyDto(sysProperty);
        return ruleDto;
    }

    @Override
    public Pager getPage(AdminUserParam param) throws ServiceException {

        Integer total = sysPropertyMapper.countPage(param.getRuleKey(),param.getKeyword(),param.getStartDate(),param.getEndDate());
        List<SysProperty> rules = sysPropertyMapper.getPage(param.getRuleKey(),param.getStart(),param.getLimit(),param.getKeyword(),param.getStartDate(),
                param.getEndDate(),param.getOrders(),param.getAscType());

        List<AdminSysPropertyDto> datas = AdminSysPropertyDto.toDtoList(rules);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);

        return pager;
    }

    @Override
    public void addRule(String ruleContent, String ruleKey) throws ServiceException {
        SysProperty sysProperty = new SysProperty();
        sysProperty.setValue(ruleContent);
        sysProperty.setKeyWord(ruleKey);
        sysProperty.setAddTime(DateStampUtils.getTimesteamp());
        sysPropertyMapper.insert(sysProperty);
    }

    @Override
    public void updateRule(Integer id, String ruleContent) throws ServiceException {
        SysProperty sysProperty = sysPropertyMapper.selectByPrimaryKey(id);
        if(sysProperty == null){
            throw new ServiceException("规则不存在！");
        }
        sysProperty.setValue(ruleContent);
        sysPropertyMapper.updateByPrimaryKey(sysProperty);
    }

    @Override
    public void deleteRule(Integer id) throws ServiceException {
        sysPropertyMapper.deleteByPrimaryKey(id);
    }
}
