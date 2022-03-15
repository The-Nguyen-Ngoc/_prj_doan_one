package com.example._prj_doan.entity;

import com.example._prj_doan.manager.constain.Constant;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends IdBasedEntity{

    @Column(nullable = false,length=45)
    private String name;
    @Column(length=128)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    private String listCategories;
    @Transient
    public String getLogoPath() {
        if(this.id == null) return "/images/img_1.png";

        return Constant.BASE_URL_AWS+ "brand-logos/" + this.id +"/"+ this.logo;
    }
}
