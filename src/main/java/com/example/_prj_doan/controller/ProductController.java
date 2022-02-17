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
import java.util.NoSuchElementException;

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
    public String saveProduct(Product product, @RequestParam("fileImage") MultipartFile multipartFile,
                              @RequestParam("extraImage") MultipartFile[] extraImage,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              RedirectAttributes redirectAttributes, Model model) throws IOException {

        setMainImageName(multipartFile, product);
        setExtraImageName(extraImage, product);
        setDetailNameAndValue(detailNames, detailValues, product);
        Product saveProduct = productService.save(product);
        saveUploadedImages(multipartFile, extraImage, saveProduct);

        redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công!");
        return "redirect:/products";
    }

    private void setDetailNameAndValue(String[] detailNames, String[] detailValues, Product product) {
        if (detailNames != null && detailValues != null) {
            for (int i = 0; i < detailNames.length; i++) {
                if (!detailNames[i].isEmpty() && !detailValues[i].isEmpty()) {
                    product.addDetail(detailNames[i], detailValues[i]);
                } else continue;
            }
        }
    }

    private void saveUploadedImages(MultipartFile multipartFile, MultipartFile[] extraImages,
                                    Product saveProduct) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "product-images/" + saveProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        if (extraImages.length > 0) {
            String uploadDir = "product-images/" + saveProduct.getId() + "/extras";

            for (MultipartFile extraImage : extraImages) {
                if (!extraImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                    FileUploadUtil.saveFile(uploadDir, fileName, extraImage);

                }
            }
        }
    }

    private void setExtraImageName(MultipartFile[] extraImage, Product product) {
        if (extraImage.length > 0) {
            for (MultipartFile multipartFile : extraImage) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImages(fileName);
                }
            }
        }

    }

    private void setMainImageName(MultipartFile mainImageName, Product product) {
        if (!mainImageName.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageName.getOriginalFilename());
            product.setMainImage(fileName);
        }
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
    public String deleteBrand(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        productService.delete(id);
        String uploadDirExtra = "product-images/" + id + "/extras";
        String uploadDir = "product-images/" + id;

        FileUploadUtil.cleanDir(uploadDirExtra);
        FileUploadUtil.cleanDir(uploadDir);
        redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            List<Brand> brands = brandService.listAllNameAndId();
            Integer numberExtraImages = product.getImages().size();
            model.addAttribute("product", product);
            model.addAttribute("brands", brands);
            model.addAttribute("numberExtraImages", numberExtraImages);
            return "products/product_form";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy sản phẩm!");
            return "redirect:/products";

        }
    }
}
