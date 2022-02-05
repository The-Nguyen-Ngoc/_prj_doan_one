package com.example._prj_doan.service;

import com.example._prj_doan.entity.Category;
import com.example._prj_doan.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> listAll() {
        return categoryRepo.findAll(Sort.by("name").ascending());
    }


    public List<Category> lisCategoriesUsedInForm() {
        List<Category> categoriesInform = new ArrayList<>();

        List<Category> categoriesInDB = categoryRepo.findAll(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {

                categoriesInform.add(new Category(category.getName()));

            } else {
                String name = "";
                name = " [ "+ category.getParent().getName() + " ] "+ category.getName() ;
                categoriesInform.add(new Category(name));
            }
        }
        return categoriesInform;
    }


    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    public Category findById(Integer id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean creating = (id == null || id == 0);

        Category categoryByName = categoryRepo.findByName(name);

        if (creating) {
            if (categoryByName != null) {
                return "Tên danh mục đã tồn tại";
            } else {
                Category categoryByAlias = categoryRepo.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "Tên Alias đã tồn tại";
                }
            }
        } else {
            if (categoryByName != null && !categoryByName.getId().equals(id)) {
                return "Tên danh mục đã tồn tại";
            }
            Category categoryByAlias = categoryRepo.findByAlias(alias);
            if (categoryByAlias != null && !categoryByAlias.getId().equals(id)) {
                return "Tên Alias đã tồn tại";
            }
        }

        return "OK";
    }

    public void delete(Integer id) {
        categoryRepo.deleteById(id);
    }

    @Transactional
    public void updateEnabledStatus(Integer id, boolean enabled) {
        categoryRepo.updateEnabledStatus(id, enabled);
    }
}
