package com.example._prj_doan.restController;

import com.example._prj_doan.dto.CategoryDTO;
import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.entity.Category;
import com.example._prj_doan.exception.BrandNotFoundException;
import com.example._prj_doan.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BrandRestController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/checkUnique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer id) throws BrandNotFoundException {
        List<CategoryDTO> categoriesDTO = new ArrayList<>();
        Brand brand = brandService.getBrandById(id);
        List<Category> categories = brand.getCategories();
        for(Category category : categories) {
            CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
            categoriesDTO.add(categoryDTO);
        }
        return categoriesDTO;
    }
}
