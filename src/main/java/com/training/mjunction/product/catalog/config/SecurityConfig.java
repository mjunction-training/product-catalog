package com.training.mjunction.product.catalog.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		return new MapReactiveUserDetailsService(
				User.withUsername("user").password("{noop}user").roles("USER").build());
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
		http.authorizeExchange().matchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
				.matchers(EndpointRequest.toAnyEndpoint()).hasRole("USER").pathMatchers("/api/**").permitAll()
				.anyExchange().authenticated();
		http.csrf().disable();
		http.logout().disable();
		http.addFilterAt(new CORSFilter(), SecurityWebFiltersOrder.CSRF);
		http.headers().cache().disable();
		return http.build();
	}

	public static final class CORSFilter implements WebFilter {

		@Override
		public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {

			log.info("Adding CORS Headers.....................");

			exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
			exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			exchange.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
			exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers",
					"X-PINGOTHER,Content-Type,X-Requested-With,accept,"
							+ "Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
			exchange.getResponse().getHeaders().add("Access-Control-Expose-Headers", "xsrf-token");

			return chain.filter(exchange);
		}
	}
}