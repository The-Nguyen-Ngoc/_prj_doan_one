package com.example._prj_doan.service;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.repository.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepo brandRepo;

    public List<Brand> listAll(){
        List<Brand> brands = brandRepo.findAll();
        return  brands;
    }

    @Modifying
    @Transactional
    public Brand save(Brand brand) {
        return brandRepo.save(brand);
    }

    public void delete(Integer id) {
        Brand brand = brandRepo.findById(id).get();
        brandRepo.delete(brand);
    }

    public Brand findById(Integer id) {
        return brandRepo.findById(id).get();
    }

    public String checkUnique(Integer id, String name) {
        boolean creating = (id == null || id == 0);

        Brand brand = brandRepo.findByName(name);
        if (creating) {
            if (brand != null) {
                return "Tên nhà sản xuất đã tồn tại";
            }else return "OK";
        }else {
            if (brand != null && !brand.getId().equals(id)) {
                return "Tên nhà sản xuất đã tồn tại";
            }
            else return "OK";
        }
    }

    public List<Brand> filterBrands(String keyword) {
        return brandRepo.filterByName(keyword);
    }
}
