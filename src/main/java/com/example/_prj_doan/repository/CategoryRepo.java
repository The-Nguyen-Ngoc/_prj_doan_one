package com.example._prj_doan.repository;

import com.example._prj_doan.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent.id is null")
    List<Category> findRootCategories(Sort sort);

    List<Category> findAll(Sort sort);

    Category findByName(String name);

    Category findByAlias(String alias);

    @Query("update Category c set c.enabled = ?2 where c.id =?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
}
