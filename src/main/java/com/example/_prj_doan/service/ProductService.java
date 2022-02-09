package com.example._prj_doan.service;

import com.example._prj_doan.entity.Product;
import com.example._prj_doan.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> listAll(){
        return (List<Product>) productRepo.findAll();
    }
}
