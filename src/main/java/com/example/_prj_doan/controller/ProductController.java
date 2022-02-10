package com.example._prj_doan.controller;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.entity.Product;
import com.example._prj_doan.service.BrandService;
import com.example._prj_doan.service.ProductService;
import com.example._prj_doan.utils.FileUploadUtil;
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

import java.io.IOException;
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
    public String saveProduct(Product product, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes, Model model) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setMainImage(fileName);

            Product saveProduct = productService.save(product);
            String uploadDir = "product-images/" + saveProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            productService.save(product);
        }

        redirectAttributes.addFlashAttribute("message", "Lưu nhãn hiệu thành công!");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{enabled}")
    public String updateEnabledProduct(@PathVariable(name = "id") Integer id, @PathVariable(name =
            "enabled") boolean enabled, RedirectAttributes redirectAttributes) {
        try {
            productService.updateEnabledStatus(id);
            if (enabled) {
                redirectAttributes.addFlashAttribute("message", "Kích Hoạt Sản Phẩm Thành Công!");
                return "redirect:/products";
            } else {
                redirectAttributes.addFlashAttribute("message", "Bỏ Kích Hoạt Sản Phẩm Thành " +
                        "Công!");
                return "redirect:/products";
            }
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/delete/{id}")
    public String deleteBrand(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        return "redirect:/products";
    }
}
