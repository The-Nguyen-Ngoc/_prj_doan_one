package com.example._prj_doan.controller;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.entity.Category;
import com.example._prj_doan.service.BrandService;
import com.example._prj_doan.service.CategoryService;
import com.example._prj_doan.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/brands")
    public String getAllBrands(Model model){
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);
        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String addBrand(Model model){
        List<Category> categories = categoryService.lisCategoriesUsedInForm();

        model.addAttribute("categories", categories);
        model.addAttribute("brand", new Brand());

        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile file,
                               RedirectAttributes redirectAttributes) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        brand.setLogo(fileName);

        Brand savedBrand = brandService.save(brand);
        String uploadDir = "brand-logos/" + savedBrand.getId();

        FileUploadUtil.cleanDir(uploadDir);
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        redirectAttributes.addFlashAttribute("message", "Saved brand successfully!");
        return "redirect:/brands";
    }

}
