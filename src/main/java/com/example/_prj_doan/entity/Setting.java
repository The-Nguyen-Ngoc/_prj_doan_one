package com.example._prj_doan.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="settings")
@Getter
@Setter
@NoArgsConstructor
public class Setting {

    @Id
    @Column(name = "`key`",nullable = false,length = 128)
    private String key;

    @Column(nullable = false,length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private SettingCategory category;

    public Setting(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return key.equals(setting.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
