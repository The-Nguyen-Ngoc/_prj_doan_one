package com.example._prj_doan.manager.controller;

import com.example._prj_doan.AmazonS3Util;
import com.example._prj_doan.manager.config.LoginUserDetails;
import com.example._prj_doan.manager.constain.Constant;
import com.example._prj_doan.entity.*;
import com.example._prj_doan.manager.service.BrandService;
import com.example._prj_doan.manager.service.CategoryService;
import com.example._prj_doan.manager.service.ProductService;
import com.example._prj_doan.manager.utils.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listAll(Model model) {
        return listByPage(1, model, "name", "asc", null,0);
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> brandsNameAndId = brandService.listAllNameAndId();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);


        model.addAttribute("product", product);
        model.addAttribute("brands", brandsNameAndId);
        model.addAttribute("numberExtraImages", 0);
        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImage,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                              RedirectAttributes redirectAttributes, Model model) throws IOException {
        if(loginUserDetails.hasRole("Salesperson")){
            productService.saveProductPrice(product);
            redirectAttributes.addFlashAttribute("message", "L??u s???n ph???m th??nh c??ng!");
            return "redirect:/products";
        }

        setMainImageName(multipartFile, product);
        setExistingExtraImageNames(imageIDs, imageNames, product);
        setNewExtraImageName(extraImage, product);
        setDetailNameAndValue(detailIDs, detailNames, detailValues, product);


        Product saveProduct = productService.save(product);
        saveUploadedImages(multipartFile, extraImage, saveProduct);

        deleteExtraImagesWeredRemoveOnForm(product);

        redirectAttributes.addFlashAttribute("message", "L??u s???n ph???m th??nh c??ng!");
        return "redirect:/products";
    }

    private void deleteExtraImagesWeredRemoveOnForm(Product product) {
        String extraImageDir = "product-images/" + product.getId() + "/extras";
        List<String> listObjectKeys = AmazonS3Util.listFolder(extraImageDir);

        for (String objectKey : listObjectKeys) {
            int lastIndexOfSlash = objectKey.lastIndexOf("/");
            String fileName = objectKey.substring(lastIndexOfSlash + 1, objectKey.length());

            if (!product.containsImageName(fileName)) {
                AmazonS3Util.deleteFile(objectKey);
                System.out.println("Deleted extra image: " + objectKey);
            }
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

        if (imageIDs == null || imageNames == null) return;

        Set<ProductImage> productImageSet = new HashSet<>();
        for (int i = 0; i < imageIDs.length; i++) {
            Integer id = Integer.parseInt(imageIDs[i]);
            String name = imageNames[i];
            productImageSet.add(new ProductImage(id, name, product));
        }

        product.setImages(productImageSet);

    }

    private void setDetailNameAndValue(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;
        for (int i = 0; i < detailNames.length; i++) {
            Integer id = Integer.parseInt(detailIDs[i]);

            if (id != 0) {
                product.addDetail(id, detailNames[i], detailValues[i]);
            } else if (!detailNames[i].isEmpty() && !detailValues[i].isEmpty()) {
                product.addDetail(detailNames[i], detailValues[i]);
            }
        }
    }

    private void saveUploadedImages(MultipartFile multipartFile, MultipartFile[] extraImages,
                                    Product saveProduct) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "product-images/" + saveProduct.getId();

            List<String> listObjectKeys = AmazonS3Util.listFolder(uploadDir+"/");
            for(String objectKey : listObjectKeys) {
                if(!objectKey.contains("/extras/")) {
                    AmazonS3Util.deleteFile(objectKey);
                }
            }
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        if (extraImages.length > 0) {
            String uploadDir = "product-images/" + saveProduct.getId() + "/extras";

            for (MultipartFile extraImage : extraImages) {
                if (!extraImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                    AmazonS3Util.uploadFile(uploadDir, fileName, extraImage.getInputStream());

                }
            }
        }
    }

    private void setNewExtraImageName(MultipartFile[] extraImage, Product product) {
        if (extraImage.length > 0) {
            for (MultipartFile multipartFile : extraImage) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if (!product.containsImageName(fileName)) {
                        product.addExtraImages(fileName);
                    }
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
                redirectAttributes.addFlashAttribute("message", "K??ch Ho???t S???n Ph???m Th??nh C??ng!");
                return "redirect:/products";
            } else {
                redirectAttributes.addFlashAttribute("message", "B??? K??ch Ho???t S???n Ph???m Th??nh " +
                        "C??ng!");
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
        redirectAttributes.addFlashAttribute("message", "X??a s???n ph???m th??nh c??ng!");
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
            redirectAttributes.addFlashAttribute("message", "Kh??ng t??m th???y s???n ph???m!");
            return "redirect:/products";

        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            model.addAttribute("product", product);
            return "products/product_detail_modal";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("message", "Kh??ng t??m th???y s???n ph???m!");
            return "redirect:/products";

        }
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
                             @RequestParam("sortField") String sortField,
                             @RequestParam("sortDir") String sortDir,

                             @RequestParam("keyword") String keyword,
                             @RequestParam("categoryId") Integer categoryId){
        Page<Product> productPages = productService.listByPage(pageNum, sortField, sortDir, keyword,categoryId);

        List<Category> categories = categoryService.lisCategoriesUsedInForm();
        long startCount = (pageNum-1)* Constant.PRODUCTS_IN_PAGE + 1;
        long endCountExpected = startCount + Constant.PRODUCTS_IN_PAGE -1;
        long endCount = (endCountExpected > productPages.getTotalElements()) ?
                productPages.getTotalElements() : endCountExpected;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        if(categoryId != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("startCount", startCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalsPage", productPages.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("products", productPages.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("totalItems", productPages.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "products/products";
    }

}
