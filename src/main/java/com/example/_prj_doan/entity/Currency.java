package com.example._prj_doan.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "currencies")
@Getter
@Setter
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false,length = 3)
    private String symbol;

    @Column(nullable = false, length = 4)
    private String code;

    @Override
    public String toString() {
        return name + " - " + code + " - " + symbol;
    }
}
