package com.unir.products.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unir.products.data.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface ProductJpaRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	List<Product> findByName(String name);

	List<Product> findByCountry(String country);

	List<Product> findByVisible(Boolean visible);

	List<Product> findByNameAndCountry(String name, String country);

}
