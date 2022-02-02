package com.example._prj_doan.repository;

import com.example._prj_doan.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends PagingAndSortingRepository<Brand, Integer> {

    
}
