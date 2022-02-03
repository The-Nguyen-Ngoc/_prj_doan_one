package com.example._prj_doan.service;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.repository.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepo brandRepo;

    public List<Brand> listAll(){
        List<Brand> brands = brandRepo.findAll();
        return  brands;
    }
}
