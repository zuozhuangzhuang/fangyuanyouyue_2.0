package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.MinimsgFormidMapper;
import com.fangyuanyouyue.user.model.MinimsgFormid;
import com.fangyuanyouyue.user.service.MiniMsgFormIdService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "miniMsgFormIdService")
public class MiniMsgFormIdServiceImpl implements MiniMsgFormIdService {
    @Autowired
    private MinimsgFormidMapper minimsgFormidMapper;

    @Override
    public void addFormId(Integer userId, String formId) throws ServiceException {
        MinimsgFormid minimsgFormid = new MinimsgFormid();
        minimsgFormid.setUserId(userId);
        minimsgFormid.setFormId(formId);
        minimsgFormid.setAddTime(DateStampUtils.getTimesteamp());
        minimsgFormidMapper.insert(minimsgFormid);
    }

    @Override
    public String getFormId(Integer userId) throws ServiceException {
        MinimsgFormid minimsgFormid = minimsgFormidMapper.getFormId(userId);
        if(minimsgFormid != null){
            //获取formId，并删除此formId（失效）
            String formId = minimsgFormid.getFormId();
            minimsgFormidMapper.deleteByPrimaryKey(minimsgFormid.getId());
            return formId;
        }
        return null;
    }
}
