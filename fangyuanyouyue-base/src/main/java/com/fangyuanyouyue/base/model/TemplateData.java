package com.fangyuanyouyue.base.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TemplateData {
    private String value;
    private String color;

    public TemplateData() {
    }

    public TemplateData(String value) {
        this.value = value;
    }

    public TemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }
}
