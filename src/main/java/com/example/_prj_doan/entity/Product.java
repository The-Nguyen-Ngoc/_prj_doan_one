package com.example._prj_doan.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,length = 256,nullable = false)
    private String name;
    @Column(unique = true,length = 256,nullable = false)
    private String alias;
    @Column(length = 512,nullable = false, name = "short_description")
    private String shortDescription;
    @Column(length = 5000,nullable = false, name = "full_description")
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;
    @Column(name="in_stock")
    private boolean inStock;

    private float cost;
    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;
    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<ProductImage> images = new HashSet<>();
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;

    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public String toString() {
        return "Product{}";
    }

    public void addExtraImages(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    @Transient
    public String getMainImagePath(){
        if(id == null|| mainImage == null) return "/images/category.png";

        return "/product-images/"+this.id+"/"+this.mainImage;
    }
}
