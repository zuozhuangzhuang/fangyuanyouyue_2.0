package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.RuleMapper;
import com.fangyuanyouyue.user.dto.RuleDto;
import com.fangyuanyouyue.user.dto.admin.AdminRuleDto;
import com.fangyuanyouyue.user.model.Rule;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "ruleService")
public class RuleServiceImpl implements RuleService{
    @Autowired
    private RuleMapper ruleMapper;

    @Override
    public RuleDto getInviteRule(Integer ruleType) throws ServiceException {
        Rule ruleByType = ruleMapper.getRuleByType(ruleType);
        if(ruleByType == null){
            throw new ServiceException("规则类型错误！");
        }
        RuleDto ruleDto = new RuleDto(ruleByType);
        return ruleDto;
    }

    @Override
    public Pager getPage(AdminUserParam param) throws ServiceException {

        Integer total = ruleMapper.countPage(param.getRuleType(),param.getKeyword(),param.getStartDate(),param.getEndDate());
        List<Rule> rules = ruleMapper.getPage(param.getRuleType(),param.getStart(),param.getLimit(),param.getKeyword(),param.getStartDate(),
                param.getEndDate(),param.getOrders(),param.getAscType());

        List<AdminRuleDto> datas = AdminRuleDto.toDtoList(rules);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);

        return pager;
    }

    @Override
    public void addRule(String ruleContent, Integer ruleType) throws ServiceException {
        Rule rule = new Rule();
        rule.setRuleContent(ruleContent);
        rule.setRuleType(ruleType);
        rule.setAddTime(DateStampUtils.getTimesteamp());
        ruleMapper.insert(rule);
    }

    @Override
    public void updateRule(Integer id, String ruleContent) throws ServiceException {
        Rule rule = ruleMapper.selectByPrimaryKey(id);
        if(rule == null){
            throw new ServiceException("规则不存在！");
        }
        rule.setRuleContent(ruleContent);
        ruleMapper.updateByPrimaryKey(rule);
    }

    @Override
    public void deleteRule(Integer id) throws ServiceException {
        ruleMapper.deleteByPrimaryKey(id);
    }
}
