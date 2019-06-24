package com.training.mjunction.product.catalog.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.training.mjunction.product.catalog.data.documents.Product;
import com.training.mjunction.product.catalog.data.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Transactional
@CacheConfig(cacheNames = "products_cache", cacheManager = "cacheManager")
public class ProductController {

	@Autowired
	private ProductRepository repository;

	@Caching(cacheable = @Cacheable(key = "#name", unless = "#result == null"), put = @CachePut(key = "#name", unless = "#result == null"))
	@RequestMapping(method = RequestMethod.GET, value = "/api/v1/products/{name}")
	public Product findByName(@PathVariable("name") final String name) {
		log.info(String.format("name(%s)", name));
		return repository.findByName(name);
	}

	@Caching(cacheable = @Cacheable(key = "#name", unless = "#result == null"), put = @CachePut(key = "#name", unless = "#result == null"))
	@RequestMapping(method = RequestMethod.GET, value = "/api/v1/products/{category}/{name}")
	public Product findByCategoryAndName(@PathVariable("category") final String category,
			@PathVariable("name") final String name) {
		log.info(String.format("name(%s) category(%s)", name, category));
		return repository.findByNameAndCategory(name, category);
	}

	@Caching(cacheable = @Cacheable(key = "#root.methodName", unless = "#result == null"), put = {
			@CachePut(key = "#root.methodName", unless = "#result == null") })
	@RequestMapping(method = RequestMethod.GET, value = "/api/v1/products")
	public List<Product> findAll() {

		log.info(() -> "Product.findAll()");

		final List<Product> products = repository.findAll();

		log.info(() -> "Returning products from findAll " + products);

		return products;

	}

	@Caching(put = @CachePut(key = "#product.name", unless = "#result == null"))
	@RequestMapping(method = RequestMethod.PUT, value = "/api/v1/products")
	public Product add(@RequestBody final Product product) {
		log.info(String.format("Product.add(%s)", product));
		return repository.save(product);
	}

	@Caching(put = @CachePut(key = "#name", unless = "#result == null"))
	@RequestMapping(method = RequestMethod.POST, value = "/api/v1/products/{name}")
	public Product update(@PathVariable("name") final String name, @RequestBody final Product product) {

		log.info(String.format("Product.add(%s)", product));

		final Product dbOne = repository.findByName(name);

		if (null == dbOne) {
			throw new IllegalArgumentException("Product not found name  :" + name);
		}

		dbOne.setName(product.getName());
		dbOne.setCategory(product.getCategory());
		dbOne.setFeatures(product.getFeatures());

		return repository.save(dbOne);
	}

	@CacheEvict(allEntries = true)
	@RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/products/{name}")
	public void delete(@PathVariable("name") final String name) {

		log.info(String.format("Product.delete(%s)", name));

		final Product dbOne = repository.findByName(name);

		if (null == dbOne) {
			throw new IllegalArgumentException("Product not found name  :" + name);
		}

		repository.delete(dbOne);
	}

}
