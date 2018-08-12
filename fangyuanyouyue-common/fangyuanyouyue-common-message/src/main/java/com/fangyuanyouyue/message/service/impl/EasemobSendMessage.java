package com.fangyuanyouyue.message.service.impl;

import org.springframework.stereotype.Service;

import com.fangyuanyouyue.message.easemob.EasemobAPI;
import com.fangyuanyouyue.message.easemob.OrgInfo;
import com.fangyuanyouyue.message.easemob.ResponseHandler;
import com.fangyuanyouyue.message.easemob.TokenUtil;
import com.fangyuanyouyue.message.service.SendMessageAPI;

import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;


@Service(value = "easemobSendMessage")
public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
