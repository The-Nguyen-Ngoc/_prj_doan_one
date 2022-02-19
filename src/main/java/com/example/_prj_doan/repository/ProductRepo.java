package com.example._prj_doan.repository;

import com.example._prj_doan.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepo extends PagingAndSortingRepository<Product, Integer>{

    Product findByName(String name);
}
