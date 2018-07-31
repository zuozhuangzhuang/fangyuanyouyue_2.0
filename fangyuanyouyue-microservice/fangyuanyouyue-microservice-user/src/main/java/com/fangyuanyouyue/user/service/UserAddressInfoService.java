package com.fangyuanyouyue.user.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.UserAddressDto;

/**
 * 收货相关接口
 */
public interface UserAddressInfoService {
    /**
     * 添加收货地址
     * @param token
     * @param receiverName
     * @param receiverPhone
     * @param province
     * @param city
     * @param area
     * @param address
     * @param postCode
     * @param type
     * @return
     * @throws ServiceException
     */
    List<UserAddressDto> addAddress(String token, String receiverName, String receiverPhone, String province, String city, String area, String address, String postCode, Integer type) throws ServiceException;

    /**
     * 修改收货地址
     * @param token
     * @param addressId
     * @param receiverName
     * @param receiverPhone
     * @param province
     * @param city
     * @param area
     * @param address
     * @param postCode
     * @param type
     * @return
     */
    UserAddressDto updateAddress(String token,Integer addressId,String receiverName,String receiverPhone,String province,String city,String area,String address,String postCode,Integer type) throws ServiceException;

    /**
     * 删除收货地址
     * @param token
     * @param addressId
     * @return
     * @throws ServiceException
     */
    List<UserAddressDto> deleteAddress(String token,Integer addressId) throws ServiceException;

    /**
     * 设置默认收货地址
     * @param token
     * @param addressId
     * @throws ServiceException
     */
    void defaultAddress(String token,Integer addressId) throws ServiceException;

    /**
     * 获取收货地址列表
     * @param token
     * @return
     * @throws ServiceException
     */
    List<UserAddressDto> getAddressList(String token,Integer addressId) throws ServiceException;

    /**
     * 获取默认地址
     * @param token
     * @return
     * @throws ServiceException
     */
    UserAddressDto getDefaultAddress(String token) throws ServiceException;
}
