package com.example._prj_doan.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StateDto {
    private Integer id;
    private String name;

    public StateDto(Integer id) {
        this.id = id;
    }
}
