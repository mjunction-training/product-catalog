package com.training.mjunction.product.catalog.handler;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.catalog.data.documents.Product;
import com.training.mjunction.product.catalog.data.repository.ReactiveProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

	@Autowired
	private ReactiveProductRepository productRepository;

	/**
	 * GET ALL Products.
	 *
	 * @param request a
	 *                {@link org.springframework.web.reactive.function.server.ServerRequest}
	 *                object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> getAll(final ServerRequest request) {

		// parse query parameter product name
		final String productName = request.queryParam("productName").orElseGet(() -> null);

		// parse query parameter category name
		final String categoryName = request.queryParam("categoryName").orElseGet(() -> null);

		Flux<Product> products = Flux.empty();

		if (isBlank(productName) && isBlank(categoryName)) {

			// fetch all products from repository
			products = productRepository.findAll();

		}

		if (isNotBlank(productName) && isNotBlank(categoryName)) {

			// fetch all products from repository
			products = productRepository.findByNameAndCategory(productName, categoryName);

		}

		if (isNotBlank(productName)) {

			// fetch all products from repository
			products = productRepository.findByName(productName);

		}

		if (isNotBlank(categoryName)) {

			// fetch all products from repository
			products = productRepository.findByCategory(categoryName);

		}

		// build response
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, Product.class);

	}

	/**
	 * GET a Product by ID.
	 *
	 * @param request a
	 *                {@link org.springframework.web.reactive.function.server.ServerRequest}
	 *                object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> getProduct(final ServerRequest request) {

		// parse path-variable
		final String productId = request.pathVariable("id");

		// build notFound response
		final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get product from repository
		final Mono<Product> productMono = productRepository.findById(productId);

		// build response
		return productMono.flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(product))).switchIfEmpty(notFound);
	}

	/**
	 * POST a Product.
	 *
	 * @param request a
	 *                {@link org.springframework.web.reactive.function.server.ServerRequest}
	 *                object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> postProduct(final ServerRequest request) {

		final Mono<Product> monoProduct = request.bodyToMono(Product.class);

		return ServerResponse.ok().build(monoProduct.doOnNext(productRepository::save).then());

	}

	/**
	 * PUT a Product.
	 *
	 * @param request a
	 *                {@link org.springframework.web.reactive.function.server.ServerRequest}
	 *                object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> putProduct(final ServerRequest request) {

		// parse id from path-variable
		final String productId = request.pathVariable("id");

		// get product data from request object
		final Mono<Product> monoProduct = request.bodyToMono(Product.class);

		monoProduct.doOnNext(b -> b.id(productId)).then();

		// get product from repository
		final Mono<Product> responseMono = monoProduct.doOnNext(productRepository::save);

		// build response
		return responseMono.flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(product)));

	}

	/**
	 * DELETE a Product.
	 *
	 * @param request a ServerRequest object
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> deleteProduct(final ServerRequest request) {

		// parse id from path-variable
		final String productId = request.pathVariable("id");

		productRepository.deleteById(productId);

		// get product from repository
		final Mono<String> responseMono = Mono.just("Delete Succesfully!");

		// build response
		return responseMono.flatMap(strMono -> ServerResponse.accepted().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject(strMono)));

	}

}
