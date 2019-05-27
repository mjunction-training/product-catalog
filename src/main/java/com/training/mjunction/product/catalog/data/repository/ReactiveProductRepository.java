
package com.training.mjunction.product.catalog.data.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.training.mjunction.product.catalog.data.documents.Product;

import reactor.core.publisher.Flux;

@Repository
public interface ReactiveProductRepository extends ReactiveMongoRepository<Product, String> {

	Flux<Product> findByName(String productName);

	Flux<Product> findByCategory(String categoryName);

	Flux<Product> findByNameAndCategory(String productName, String categoryName);

}
