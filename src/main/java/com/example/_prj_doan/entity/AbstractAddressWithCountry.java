package com.example._prj_doan.entity;

import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@Data
@MappedSuperclass
public class AbstractAddressWithCountry extends AbstractAddress {
    @ManyToOne
    @JoinColumn(name = "country_id")
    protected Country country;

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address +=" " + lastName;
        if(!address.isEmpty()) address += ", "+ addressLine1;
        if(addressLine2 != null && !addressLine2.isEmpty()) address += ", "+ addressLine2;
        if(!city.isEmpty()) address += ", "+ city;
        if(state !=null&& !state.isEmpty()) address += ", "+ state;
        address += ", " + country.getName();
        if(!postalCode.isEmpty()) address += ". Mã Bưu Điện: "+ postalCode;
        if(!phoneNumber.isEmpty()) address += ". Số Điện Thoại: "+ phoneNumber;
        return address;
    }
}
