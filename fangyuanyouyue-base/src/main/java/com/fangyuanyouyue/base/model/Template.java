package com.fangyuanyouyue.base.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
  * 模版消息结构
  * @author why
  *
  */
@Getter
@Setter
@ToString
public class Template {
    private String touser;//接收人的openId
    private String template_id;//模版id
    private String page;//点击模版访问url
    private String form_id;//表单提交formId
    private Map<String,TemplateData> data;//消息内容
}

