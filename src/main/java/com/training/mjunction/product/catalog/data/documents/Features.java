package com.training.mjunction.product.catalog.data.documents;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@Document(collection = "features")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "properties" })
public class Features implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty("id")
	private String id = UUID.randomUUID().toString();

	@Field("properties")
	@JsonProperty("properties")
	private Map<String, Object> properties;
}
