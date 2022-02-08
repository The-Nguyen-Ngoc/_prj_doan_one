package com.example._prj_doan.testRepo;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.entity.Category;
import com.example._prj_doan.entity.Product;
import com.example._prj_doan.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE )
@Rollback(value = false)
public class ProductRepoTests {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = testEntityManager.find(Brand.class, 1);
        Category category = testEntityManager.find(Category.class, 1);

        Product product = new Product();
        product.setName("Product 1");
        product.setAlias("product-1");
        product.setPrice(100000);
        product.setShortDescription("Description 1");
        product.setFullDescription("Full description 1");
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());
        Product product1 = productRepo.save(product);

        assertThat(product1).isNotNull();
        assertThat(product1.getId()).isGreaterThan(0);
        // given
        // when
        // then
    }

    @Test
    public void testListAll(){
        Iterable<Product> products = productRepo.findAll();
        products.forEach(System.out::println);
    }
}
