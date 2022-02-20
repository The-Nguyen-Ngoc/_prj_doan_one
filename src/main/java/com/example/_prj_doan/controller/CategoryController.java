package com.example._prj_doan.controller;


import com.example._prj_doan.entity.Category;
import com.example._prj_doan.entity.User;
import com.example._prj_doan.service.CategoryService;
import com.example._prj_doan.utils.CategoryCsvExporter;
import com.example._prj_doan.utils.FileUploadUtil;
import com.example._prj_doan.utils.UserCsvExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model) {
        List<Category> categories = categoryService.listAll();
        model.addAttribute("categories", categories);
        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> categoriesInForm = categoryService.lisCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("categoriesInForm", categoriesInForm);
        return "categories/category_form";
    }


    //code dở save Category
    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile file,
                               RedirectAttributes redirectAttributes) throws IOException {
        if(!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename()).replace(" ", "");
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        }else {
            if(category.getImage().isEmpty()) category.setImage(null);
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "Saved category successfully!");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        List<Category> categoriesInForm = categoryService.lisCategoriesUsedInForm();
        try {
            Category categoryEdit = categoryService.findById(id);
            model.addAttribute("category", categoryEdit);
            model.addAttribute("pageTitle", "Cập Nhật Danh Mục");
            model.addAttribute("categoriesInForm", categoriesInForm);
            return "categories/category_form";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Xóa Danh Mục Thành Công!");
            return "redirect:/categories";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{enabled}")
    public String updateEnabledCategory(@PathVariable(name = "id") Integer id, @PathVariable(name =
            "enabled") boolean enabled, RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateEnabledStatus(id, enabled);
            if (enabled) {
                redirectAttributes.addFlashAttribute("message", "Kích Hoạt Danh Mục Thành Công!");
                return "redirect:/categories";
            } else {
                redirectAttributes.addFlashAttribute("message", "Bỏ Kích Hoạt Danh Mục Thành " +
                        "Công!");
                return "redirect:/categories";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Category> userCate = categoryService.listAll();
        CategoryCsvExporter categoryCsvExporter = new CategoryCsvExporter();
        categoryCsvExporter.export(userCate,response);
    }

}
