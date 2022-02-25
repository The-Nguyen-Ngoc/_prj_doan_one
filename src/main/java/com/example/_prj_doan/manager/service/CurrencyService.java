package com.example._prj_doan.manager.service;

import com.example._prj_doan.manager.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepo currencyRepository;
}
