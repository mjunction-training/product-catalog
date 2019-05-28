package com.training.mjunction.product.catalog.data.documents;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Document(collection = "product")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "category", "features" })
public class Product {

	@Id
	@JsonProperty("id")
	private String id = UUID.randomUUID().toString();

	@Field("name")
	@JsonProperty("name")
	@Indexed(unique = true, direction = IndexDirection.ASCENDING, name = "product-name")
	private String name;

	@JsonProperty("category")
	@Indexed(unique = false, direction = IndexDirection.DESCENDING, name = "product-category")
	private String category;

	@JsonProperty("features")
	@Field("features")
	private Features features;
}
