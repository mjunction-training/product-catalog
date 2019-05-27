package com.training.mjunction.product.catalog.data.documents;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Document(collection = "features")
public class Features {

	@Id
	private String id;

	@Field("properties")
	private Map<String, Object> properties;
}
