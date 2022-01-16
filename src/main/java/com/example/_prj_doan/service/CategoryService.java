package com.example._prj_doan.service;

import com.example._prj_doan.entity.Category;
import com.example._prj_doan.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> listAll(){
        return categoryRepo.findAll();
    }
}
