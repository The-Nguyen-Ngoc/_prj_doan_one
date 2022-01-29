package com.example._prj_doan.service;

import com.example._prj_doan.entity.Category;
import com.example._prj_doan.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> listAll(){
        return categoryRepo.findAll();
    }


    public List<Category> lisCategoriesUsedInForm(){
        List<Category> categoriesInform = new ArrayList<>();

        List<Category> categoriesInDB = categoryRepo.findAll();

        for(Category category : categoriesInDB) {
            if (category.getParent() == null) {

                categoriesInform.add(new Category(category.getName()));

            } else {
                String name = "";
                name = category.getParent().getName() + " [ " +category.getName()+" ] ";
                categoriesInform.add(new Category(name));
            }
        }
        return categoriesInform;
    }


}
