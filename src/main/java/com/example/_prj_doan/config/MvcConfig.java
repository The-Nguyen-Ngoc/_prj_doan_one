package com.example._prj_doan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author TheNN
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + dirName + "/**") //hien thi anh
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImagesName = "category-images";
        Path categoryImagesDir = Paths.get(categoryImagesName);

        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + categoryImagesName + "/**") //hien thi anh
                .addResourceLocations("file:/" + categoryImagesPath + "/");

        String brandLogo = "brand-logos";
        Path brandLogoDir = Paths.get(brandLogo);

        String brandLogosPath = brandLogoDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + brandLogo + "/**") //hien thi anh
                .addResourceLocations("file:/" + brandLogosPath + "/");

        String productImages = "product-images";
        Path productImageDir = Paths.get(productImages);

        String productImagePath = productImageDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + productImages + "/**") //hien thi anh
                .addResourceLocations("file:/" + productImagePath + "/");
    }
}
