package com.example._prj_doan.service;

import com.example._prj_doan.entity.Product;
import com.example._prj_doan.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> listAll() {
        return (List<Product>) productRepo.findAll();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else product.setAlias(product.getAlias().replaceAll(" ", "-"));

        product.setUpdatedTime(new Date());

        return productRepo.save(product);
    }

    public String checkUnique(Integer id, String name) {
        boolean createProduct = (id == null || id == 0);
        Product product = productRepo.findByName(name);

        if (createProduct) {
            if (product != null) {
                return "Duplicated";
            }
        } else {
            if (product != null && product.getId()!=id) {
                return "Duplicated";
            }
        }
        return "OK";
    }

    public void updateEnabledStatus(Integer id) {
        Product product = productRepo.findById(id).get();
        product.setEnabled(!product.getEnabled());
        productRepo.save(product);
    }

    public void delete(Integer id) {
        productRepo.deleteById(id);
    }

    public Product get(Integer id) {
        try {
            return productRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Không tìm thấy sản phẩm");
        }
    }
}
