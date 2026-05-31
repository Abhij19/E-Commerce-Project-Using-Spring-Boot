package com.ecommerce.product.repository;

import com.ecommerce.product.model.Product;
import org.apache.logging.log4j.util.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    @Query("Select p from products p where p.active = true and p.stockQuantity>0 and " +
            "(lower(p.name) like lower(concat('%', :keyword, '%')) or lower(p.description) " +
            "like lower(concat('%', :keyword, '%')) or lower(p.category) like lower(concat('%', :keyword, '%')))")
    List<Product> searchProducts(@Param("keyword") String keyword);

    Optional <Product> findByIdAndActiveTrue(Long id);
}
