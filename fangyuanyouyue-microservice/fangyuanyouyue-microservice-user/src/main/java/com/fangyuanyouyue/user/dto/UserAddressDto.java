package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.user.model.UserAddressInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户地址
 */
@Getter
@Setter
@ToString
public class UserAddressDto {
    //UserAddressInfo
    private Integer userAddresId;//收货地址ID

    private String receiverName;//收货人

    private String receiverPhone;//收货人手机号码

    private String province;//省份

    private String city;//城市

    private String area;//区域

    private String address;//详细地址

    private String postCode;//邮编

    private Integer addressType;//类型 1默认地址 2其他


    public UserAddressDto() {
    }

    public UserAddressDto(UserAddressInfo userAddressInfo) {
        this.userAddresId = userAddressInfo.getId();
        this.receiverName = userAddressInfo.getReceiverName();
        this.receiverPhone = userAddressInfo.getReceiverPhone();
        this.province = userAddressInfo.getProvince();
        this.city = userAddressInfo.getCity();
        this.area = userAddressInfo.getArea();
        this.address = userAddressInfo.getAddress();
        this.postCode = userAddressInfo.getPostCode();
        this.addressType = userAddressInfo.getType();
    }
    public static List<UserAddressDto> toDtoList(List<UserAddressInfo> list) {
        if (list == null) {
            return null;
        }
        List<UserAddressDto> dtolist = new ArrayList<>();
        for (UserAddressInfo model : list) {
            UserAddressDto dto = new UserAddressDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
