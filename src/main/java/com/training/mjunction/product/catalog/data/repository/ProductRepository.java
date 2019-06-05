package com.training.mjunction.product.catalog.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.training.mjunction.product.catalog.data.documents.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

	Product findByName(String productName);

	Product findByNameAndCategory(String productName, String categoryName);

	List<Product> findByCategory(String categoryName);

}
