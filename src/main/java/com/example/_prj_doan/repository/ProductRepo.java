package com.example._prj_doan.repository;

import com.example._prj_doan.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepo extends PagingAndSortingRepository<Product, Integer>{
    @Query("select p from Product p where p.name like ?1")
    Product findByName(String name);
}
