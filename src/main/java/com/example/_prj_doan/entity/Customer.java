package com.example._prj_doan.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractAddressWithCountry{

    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false,  length = 64)
    private String password;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;
    private boolean enabled;

    @Column(name="created_time")
    private Date createdTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token",length = 30)
    private String resetPasswordToken;

    @Transient
    public String getFullName(){
        return firstName + " " + lastName;
    }
}
