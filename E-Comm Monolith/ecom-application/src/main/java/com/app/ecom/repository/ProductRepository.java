package com.app.ecom.repository;

import com.app.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    @Query("Select p from products p where p.active = true and p.stockQuantity>0 and " +
            "(lower(p.name) like lower(concat('%', :keyword, '%')) or lower(p.description) " +
            "like lower(concat('%', :keyword, '%')) or lower(p.category) like lower(concat('%', :keyword, '%')))")
    List<Product> searchProducts(@Param("keyword") String keyword);
    // Keyword defined in param annotation goes in to the query as keyword
}
