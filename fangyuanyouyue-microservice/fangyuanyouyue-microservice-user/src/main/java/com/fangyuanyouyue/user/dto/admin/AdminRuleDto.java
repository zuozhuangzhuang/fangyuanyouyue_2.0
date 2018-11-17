package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.user.model.Rule;
import com.fangyuanyouyue.user.model.UserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则DTO
 */
@Getter
@Setter
@ToString
public class AdminRuleDto {
    private Integer id;
    //规则内容
    private String ruleContent;
    //规则类型 1邀请规则 2xx规则
    private Integer ruleType;

    public AdminRuleDto() {
    }

    public AdminRuleDto(Rule rule) {
        this.id = rule.getId();
        this.ruleContent = rule.getRuleContent();
        this.ruleType = rule.getRuleType();
    }

    public static List<AdminRuleDto> toDtoList(List<Rule> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminRuleDto> dtolist = new ArrayList<>();
        for (Rule model : list) {
            AdminRuleDto dto = new AdminRuleDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
