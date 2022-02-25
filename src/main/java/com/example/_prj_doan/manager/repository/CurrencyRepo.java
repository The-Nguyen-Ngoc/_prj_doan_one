package com.example._prj_doan.manager.repository;

import com.example._prj_doan.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepo extends CrudRepository<Currency, Integer> {

    List<Currency> findAllByOrderByNameAsc();
}
