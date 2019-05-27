package com.training.mjunction.product.catalog.data.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Document(collection = "product")
public class Product {

	@Id
	private String id;

	@Field("name")
	@Indexed(unique = true, direction = IndexDirection.ASCENDING, name = "product-name")
	private String name;

	@Indexed(unique = false, direction = IndexDirection.DESCENDING, name = "product-category")
	private String category;

	@DBRef
	@Field("features")
	private Features features;
}
