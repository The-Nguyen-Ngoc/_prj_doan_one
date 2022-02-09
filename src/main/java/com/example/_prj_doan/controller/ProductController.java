package com.example._prj_doan.controller;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.entity.Product;
import com.example._prj_doan.service.BrandService;
import com.example._prj_doan.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @GetMapping("/products")
    public String listAll(Model model) {
        model.addAttribute("products", productService.listAll());
        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> brandsNameAndId = brandService.listAllNameAndId();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);


        model.addAttribute("brands", brandsNameAndId);
        model.addAttribute("product", product);
        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, Model model) {
        System.out.printf(product.getName());;
        System.out.printf(""+product.getId());;
        System.out.println(product.getBrand().getId());
        System.out.println(product.getCategory().getId());

        return "redirect:/products";
    }
}
