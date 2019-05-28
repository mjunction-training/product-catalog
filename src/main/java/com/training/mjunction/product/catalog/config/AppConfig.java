package com.training.mjunction.product.catalog.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

	@Bean
	public ObjectMapper objetMapper() {
		return new ObjectMapper();
	}

	@Bean
	public Validator validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Bean
	public Jackson2JsonEncoder jackson2JsonEncoder(final ObjectMapper mapper) {
		return new Jackson2JsonEncoder(mapper);
	}

	@Bean
	public Jackson2JsonDecoder jackson2JsonDecoder(final ObjectMapper mapper) {
		return new Jackson2JsonDecoder(mapper);
	}

	@Bean
	public WebFluxConfigurer webFluxConfigurer(final Jackson2JsonEncoder encoder, final Jackson2JsonDecoder decoder) {
		return new WebFluxConfigurer() {
			@Override
			public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
				configurer.defaultCodecs().jackson2JsonEncoder(encoder);
				configurer.defaultCodecs().jackson2JsonDecoder(decoder);
			}
		};
	}

}
