package com.example._prj_doan.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage() {
    }

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", product=" + product +
                '}';
    }

    @Transient
    public String getImagePath() {
        return "/product-images/" +product.getId()+"/extras/"+this.name;
    }
}
