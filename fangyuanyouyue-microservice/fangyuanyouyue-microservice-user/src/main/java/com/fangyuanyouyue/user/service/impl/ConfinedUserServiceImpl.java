package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.CheckCode;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.*;
import com.fangyuanyouyue.user.dto.admin.AdminUserDto;
import com.fangyuanyouyue.user.model.*;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "confinedUserService")
public class ConfinedUserServiceImpl implements ConfinedUserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private ConfinedUserMapper confinedUserMapper;

    @Override
    public Pager getPage(AdminUserParam param) {

        Integer total = userInfoMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<UserInfo> datas = userInfoMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        ArrayList<AdminUserDto> dtos = new ArrayList<AdminUserDto>();
        for(UserInfo info:datas) {
            AdminUserDto dto = new AdminUserDto(info);
            UserInfoExt ext = userInfoExtMapper.selectByUserId(info.getId());
            dto.setFansBaseCount(ext.getFansCount());
            dto.setAuthType(ext.getAuthType());
            dto.setName(ext.getName());
            dto.setIdentity(ext.getIdentity());
            Long credit = ext.getCredit();
            Integer creditLevel = 0;
            if(credit != null){
                if(credit < -100){//差
                    creditLevel = 1;
                }else if(-100 <= credit && credit < 1000){//低
                    creditLevel = 2;
                }else if(1000 <= credit && credit < 10000){//中
                    creditLevel = 3;
                }else if(10000 <= credit && credit < 500000){//高
                    creditLevel = 4;
                }else if(500000 <= credit){//优
                    creditLevel = 5;
                }
            }
            dto.setCreditLevel(creditLevel);


            UserWallet wallet = userWalletMapper.selectByUserId(info.getId());
            dto.setBalance(wallet.getBalance());
            dto.setPoint(wallet.getPoint());


            UserVip vip = userVipMapper.getUserVipByUserId(info.getId());
            dto.setVipLevel(vip.getVipLevel());
            dto.setVipStatus(vip.getStatus());
            dto.setVipLevelDesc(vip.getLevelDesc());
            dto.setFansBaseCount(ext.getFansCount());
            dtos.add(dto);

        }
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(dtos);
        return pager;
    }

    @Override
    public void updateConfined(Integer id, Integer type, String code) throws ServiceException {
        //根据id获取用户
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if(userInfo == null){
            throw new ServiceException("未找到用户!");
        }
        ConfinedUser confinedUser = confinedUserMapper.selectByUserId(id);
        if(type.equals(Status.IS_PROXY.getValue())){
            if(confinedUser == null){
                confinedUser = new ConfinedUser();
                confinedUser.setStatus(Status.IS_PROXY.getValue());
                confinedUser.setAddTime(DateStampUtils.getTimesteamp());
                confinedUser.setUserId(id);
                String proxy = CheckCode.getProxyCode();
                confinedUser.setCode(proxy);
                if(StringUtils.isNotEmpty(code)){
                    ConfinedUser parentProxy = confinedUserMapper.selectByCode(code);
                    confinedUser.setParentId(parentProxy.getUserId());
                }
                confinedUserMapper.insert(confinedUser);
            }else{
                if(confinedUser.getStatus().equals(Status.IS_PROXY.getValue())){
                    throw new ServiceException("请勿重复设置!");
                }
                confinedUser.setStatus(Status.IS_PROXY.getValue());
                confinedUserMapper.updateByPrimaryKeySelective(confinedUser);
            }
        }else{
            //TODO 如果取消一级代理，二级代理怎么办
            if(confinedUser == null){
                throw new ServiceException("未找到代理！");
            }
            confinedUser.setStatus(Status.NOT_PROXY.getValue());
            confinedUserMapper.updateByPrimaryKeySelective(confinedUser);
        }
    }
}
