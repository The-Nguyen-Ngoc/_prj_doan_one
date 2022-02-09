package com.example._prj_doan.repository;

import com.example._prj_doan.entity.Brand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepo extends PagingAndSortingRepository<Brand, Integer> {

    @Query("SELECT b FROM Brand b ORDER BY b.name ASC")
    List<Brand> findAll();

    @Query("SELECT new Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    List<Brand> findAllNameAndId();

    @Query("select b from Brand b where b.name like ?1")
    Brand findByName(String name);

    @Query("select b from Brand b where b.name like %?1%")
    List<Brand> filterByName(String keyword);
}
