package com.fangyuanyouyue.order.dto.adminDto;

import com.fangyuanyouyue.order.model.Company;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 物流公司DTO
 */
@Getter
@Setter
@ToString
public class AdminCompanyDto {
    private Integer companyId;//公司ID
    private String companyNo;//公司编号
    private String name;//物流公司名
    private BigDecimal price;//参考价格
    private Integer status;//状态 1正常 2删除

    public AdminCompanyDto() {
    }

    public AdminCompanyDto(Company company) {
        this.companyId = company.getId();
        this.companyNo = company.getCompanyNo();
        this.name = company.getName();
        this.price = company.getPrice();
        this.status = company.getStatus();
    }

    public static List<AdminCompanyDto> toDtoList(List<Company> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AdminCompanyDto> dtolist = new ArrayList<AdminCompanyDto>();
        for (Company model : list) {
            AdminCompanyDto dto = new AdminCompanyDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
