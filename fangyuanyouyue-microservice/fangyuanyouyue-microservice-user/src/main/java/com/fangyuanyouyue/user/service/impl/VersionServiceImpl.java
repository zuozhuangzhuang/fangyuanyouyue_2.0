package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.WaterMarkUtils;
import com.fangyuanyouyue.user.dao.AppVersionMapper;
import com.fangyuanyouyue.user.dao.SysPropertyMapper;
import com.fangyuanyouyue.user.dto.AppVersionDto;
import com.fangyuanyouyue.user.dto.admin.AdminAppVersionDto;
import com.fangyuanyouyue.user.model.AppVersion;
import com.fangyuanyouyue.user.model.SysProperty;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.FileUploadService;
import com.fangyuanyouyue.user.service.VersionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service(value = "versionService")
public class VersionServiceImpl implements VersionService {
    @Autowired
    private AppVersionMapper appVersionMapper;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private SysPropertyMapper sysPropertyMapper;

    @Override
    public AppVersionDto getVersion() throws ServiceException {
        AppVersion version = appVersionMapper.getVersion();
        if(version == null){
            throw new ServiceException("未发现新版本！");
        }
        AppVersionDto appVersionDto = new AppVersionDto(version);

        SysProperty inviteRule = sysPropertyMapper.getRuleByKey(Status.INVITE_RULE.getMessage());
        if(inviteRule != null){
            appVersionDto.setIsInvite(Status.ISINVITE.getValue());
        }else{
            appVersionDto.setIsInvite(Status.NOTINVITE.getValue());
        }

        SysProperty qrRule = sysPropertyMapper.getRuleByKey(Status.QR_RULE.getMessage());
        if(qrRule != null){
            appVersionDto.setSwitchQRCode(Status.ISQRCODE.getValue());
        }else{
            appVersionDto.setSwitchQRCode(Status.NOTQRCODE.getValue());
        }
        //添加列表后缀配置
        SysProperty forumSuffix = sysPropertyMapper.getRuleByKey(Status.FORUM_SUFFIX.getMessage());
        if(forumSuffix != null){
            appVersionDto.setForumSuffix(forumSuffix.getValue());
        }
        SysProperty goodsSuffix = sysPropertyMapper.getRuleByKey(Status.GOODS_SUFFIX.getMessage());
        if(goodsSuffix != null){
            appVersionDto.setGoodsSuffix(goodsSuffix.getValue());
        }
        SysProperty shopSuffix = sysPropertyMapper.getRuleByKey(Status.SHOP_SUFFIX.getMessage());
        if(shopSuffix != null){
            appVersionDto.setShopSuffix(shopSuffix.getValue());
        }
        SysProperty videoSuffix = sysPropertyMapper.getRuleByKey(Status.VIDEO_SUFFIX.getMessage());
        if(videoSuffix != null){
            appVersionDto.setVideoSuffix(videoSuffix.getValue());
        }
        SysProperty detailSuffix = sysPropertyMapper.getRuleByKey(Status.DETAIL_SUFFIX.getMessage());
        if(detailSuffix != null){
            appVersionDto.setDetailSuffix(detailSuffix.getValue());
        }
        return appVersionDto;
    }

    @Override
    public Pager getVersionList(AdminUserParam param) throws ServiceException {
        Integer total = appVersionMapper.countPage(param.getKeyword(),param.getStartDate(),param.getEndDate());

        List<AppVersion> datas = appVersionMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(AdminAppVersionDto.toDtoList(datas));
        return pager;
    }

    @Override
    public void versionAdd(AdminUserParam param) throws ServiceException {

        MultipartFile file = param.getFile();
        String fileUrl = null;
        String fileName = fileUploadService.getFileName(file.getOriginalFilename());
        if(!fileName.endsWith("apk")){
            throw new ServiceException("请上传APK文件！");
        }
        fileName = "apk" + fileName;
        fileUrl = fileUploadService.uploadFile(file, fileUrl, fileName);

        AppVersion appVersion = new AppVersion();
        appVersion.setVersionNo(param.getVersionNo());
        appVersion.setVersionDesc(param.getVersionDesc());
        appVersion.setVersionName(param.getVersionName());
        appVersion.setVersionUrl(fileUrl);
        appVersion.setPackageName(param.getPackageName());
        appVersion.setType(param.getType());
        appVersion.setAddTime(DateStampUtils.getTimesteamp());
        appVersionMapper.insert(appVersion);
    }

    @Override
    public void versionModify(AdminUserParam param) throws ServiceException {
        AppVersion appVersion = appVersionMapper.selectByPrimaryKey(param.getId());
        if(appVersion == null){
            throw new ServiceException("未找到版本信息！");
        }
        if(param.getFile() != null){
            MultipartFile file = param.getFile();
            String fileUrl = null;
            String fileName = fileUploadService.getFileName(file.getOriginalFilename());
            if(!fileName.endsWith("akp")){
                throw new ServiceException("请上传APK文件！");
            }
            fileName = "apk" + fileName;
            fileUrl = fileUploadService.uploadFile(file, fileUrl, fileName);
            appVersion.setVersionUrl(fileUrl);
        }
        if(param.getVersionNo() == null){
            appVersion.setVersionNo(param.getVersionNo());
        }
        if(StringUtils.isNotEmpty(param.getVersionDesc())){
            appVersion.setVersionDesc(param.getVersionDesc());
        }
        if(StringUtils.isNotEmpty(param.getVersionName())){
            appVersion.setVersionName(param.getVersionName());
        }
        if(param.getType() != null){
            appVersion.setType(param.getType());
        }
        if(StringUtils.isNotEmpty(param.getPackageName())){
            appVersion.setPackageName(param.getPackageName());
        }
        appVersionMapper.updateByPrimaryKeySelective(appVersion);
    }

    @Override
    public void versionDelete(Integer versionId) throws ServiceException {
        appVersionMapper.deleteByPrimaryKey(versionId);
    }
}
