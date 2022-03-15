package com.example._prj_doan.manager.service;

import com.example._prj_doan.entity.Category;
import com.example._prj_doan.manager.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

                categoriesInform.add(new Category(category.getId(), category.getName()));

            } else {
                String name = "";
                name = " [ " + category.getParent().getName() + " ] " + category.getName();
                categoriesInform.add(new Category(category.getId(), name));
            }
        }
        return categoriesInform;
    }


    public Category save(Category category) {

        Category parent = category.getParent();
        if (parent != null) {
            String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += parent.getId() + "-";
            category.setAllParentIDs(allParentIds);
        }
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

    @Transactional
    public void delete(Integer id) {
        categoryRepo.deleteById(id);
    }

    @Transactional
    public void updateEnabledStatus(Integer id, boolean enabled) {
        categoryRepo.updateEnabledStatus(id, enabled);
    }
}
