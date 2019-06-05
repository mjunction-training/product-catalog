package com.training.mjunction.product.catalog.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "product_cache")
public class ProductController {

	@Autowired
	private ProductRepository repository;

	@Cacheable(key = "#name", unless = "#result != null")
	@RequestMapping(method = RequestMethod.GET, value = "/products/{name}")
	public Product findByName(@PathVariable("name") final String name) {
		log.info(String.format("name(%s)", name));
		return repository.findByName(name);
	}

	@Cacheable(key = "#category + #name", unless = "#result != null")
	@RequestMapping(method = RequestMethod.GET, value = "/products/{category}/{name}")
	public Product findByCategoryName(@PathVariable("category") final String category,
			@PathVariable("name") final String name) {
		log.info(String.format("name(%s) category(%s)", name, category));
		return repository.findByNameAndCategory(name, category);
	}

	@Cacheable(key = "all-products", unless = "#result != null")
	@RequestMapping(method = RequestMethod.GET, value = "/products")
	public List<Product> findAll() {
		log.info("Product.findAll()");
		return repository.findAll();
	}

	@CachePut(key = "#result.name", unless = "#result != null")
	@RequestMapping(method = RequestMethod.PUT, value = "/products")
	public Product add(@RequestBody final Product product) {
		log.info(String.format("Product.add(%s)", product));
		return repository.save(product);
	}

	@CachePut(key = "#result.name", unless = "#result != null")
	@RequestMapping(method = RequestMethod.POST, value = "/products/{name}")
	public Product update(@PathVariable("name") final String name, @RequestBody final Product product) {

		log.info(String.format("Product.add(%s)", product));

		final Product dbOne = repository.findByName(name);

		if (null == dbOne) {
			throw new IllegalArgumentException("Product not found name  :" + name);
		}

		return repository.save(dbOne.name(product.name()).category(product.category()).features(product.features()));
	}

	@CacheEvict(key = "#name", allEntries = false)
	@RequestMapping(method = RequestMethod.DELETE, value = "/products/{name}")
	public void delete(@PathVariable("name") final String name) {

		log.info(String.format("Product.delete(%s)", name));

		final Product dbOne = repository.findByName(name);

		if (null == dbOne) {
			throw new IllegalArgumentException("Product not found name  :" + name);
		}

		repository.delete(dbOne);
	}

}
