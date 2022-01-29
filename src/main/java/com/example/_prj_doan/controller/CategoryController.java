package com.example._prj_doan.controller;


import com.example._prj_doan.entity.Category;
import com.example._prj_doan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model){
        List<Category> categories = categoryService.listAll();
        model.addAttribute("categories", categories);
        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Category> categoriesInForm = categoryService.lisCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("categoriesInForm", categoriesInForm);
        return "categories/category_form";
    }
//code dở save Category
    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage")MultipartFile file){
        String fileName = StringUtils
    }
}
