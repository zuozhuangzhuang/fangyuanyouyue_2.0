package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.user.model.Rule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 规则DTO
 */
@Getter
@Setter
@ToString
public class RuleDto {
    private Integer id;
    //规则内容
    private String ruleContent;
    //规则类型 1邀请规则 2xx规则
    private Integer ruleType;

    public RuleDto() {
    }

    public RuleDto(Rule rule) {
        this.id = rule.getId();
        this.ruleContent = rule.getRuleContent();
        this.ruleType = rule.getRuleType();
    }
}
