package com.fangyuanyouyue.base.util;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;

/**
 * 解析外部调用的返回值工具类
 */
public class ParseReturnValue {


    public synchronized static BaseResp getParseReturnValue(String result){
        BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(result),BaseResp.class);
        return baseResp;
    }
}
