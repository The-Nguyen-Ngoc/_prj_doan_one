package com.example._prj_doan.manager.repository;

import com.example._prj_doan.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepo extends PagingAndSortingRepository<Product, Integer> {

    Product findByName(String name);


    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " +
            "OR p.shortDescription LIKE %?1% " +
            "OR p.fullDescription LIKE %?1% " +
            "OR p.brand.name LIKE %?1% " +
            "OR p.category.name LIKE %?1% ")
    Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p from Product p WHERE p.category.id = ?1 Or p.category.allParentIDs LIKE %?2%")
     Page<Product> findByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);


    @Query("SELECT p from Product p WHERE (p.category.id = ?1 " +
            "Or p.category.allParentIDs LIKE %?2%) AND "+
            "(p.name LIKE %?3%) "+
            "OR p.shortDescription LIKE %?3% " +
            "OR p.fullDescription LIKE %?3% " +
            "OR p.brand.name LIKE %?3% " +
            "OR p.category.name LIKE %?3%")
    Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, String keyword,
                                   Pageable pageable);

}
