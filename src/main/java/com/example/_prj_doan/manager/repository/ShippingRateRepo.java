package com.example._prj_doan.manager.repository;

import com.example._prj_doan.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ShippingRateRepo extends PagingAndSortingRepository<ShippingRate, Integer> {

    @Query("select s from ShippingRate s where s.country.id = ?1 and s.state = ?2")
    ShippingRate findByCountryAndState(Integer countryId, String state);

    @Transactional
    @Modifying
    @Query("update ShippingRate s set s.codSupported = ?2 where s.id = ?1")
    void updateCodSupported(Integer id, boolean codSupported);

    @Query("select s from ShippingRate s where s.country.name LIKE %?1% or s.state LIKE %?1%")
    Page<ShippingRate> findAll(String keyword, Pageable pageable);

    Long countById(Integer id);
}
