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
    }
}
