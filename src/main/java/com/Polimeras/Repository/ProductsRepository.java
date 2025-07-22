package com.Polimeras.Repository;

import com.Polimeras.Entity.Category;
import com.Polimeras.Entity.Products;
import com.Polimeras.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

    @Query(value = "SELECT * FROM products WHERE category = ? LIMIT 1000",nativeQuery = true)
    List<Products> getCategory(Category category);

    @Query(value = "SELECT * FROM products WHERE category = 'vegetables' LIMIT 1000",nativeQuery = true)
    List<Products> getCategory();

    List<Products> findByUser(Users user);

    List<Products> findByNameContainingIgnoreCase(String name);
}

