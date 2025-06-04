package com.unir.products.service;

import java.util.List;

import com.unir.products.data.model.Product;
import com.unir.products.controller.model.ProductDto;
import com.unir.products.controller.model.CreateProductRequest;

public interface ProductsService {
	
	List<Product> getProducts(String name, String country, String description, Boolean visible);
	
	Product getProduct(String productId);
	
	Boolean removeProduct(String productId);
	
	Product createProduct(CreateProductRequest request);

	Product updateProduct(String productId, String updateRequest);

	Product updateProduct(String productId, ProductDto updateRequest);

}
