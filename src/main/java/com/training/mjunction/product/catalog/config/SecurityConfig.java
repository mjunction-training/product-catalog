package com.training.mjunction.product.catalog.config;

import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;
import static org.springframework.http.HttpMethod.OPTIONS;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**", "/js/**", "/css/**", "/*.html", "/*.htm", "/*.jsp",
				"/swagger-ui.html");
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class).csrf().disable().authorizeRequests()
				.requestMatchers(toAnyEndpoint()).permitAll().antMatchers(OPTIONS, "/**").permitAll()
				.antMatchers("/actuator/**", "/js/**", "/css/**", "/*.html", "/*.htm", "/*.jsp").permitAll()
				.anyRequest().authenticated().and().logout().permitAll().and().httpBasic();
	}

	public static final class CORSFilter implements Filter {

		@Override
		public void init(final FilterConfig filterConfig) throws ServletException {
		}

		@Override
		public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
				throws IOException, ServletException {

			log.info("Adding CORS Headers.....................");

			final HttpServletResponse response = (HttpServletResponse) res;

			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER,Content-Type,X-Requested-With,accept,"
					+ "Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
			response.setHeader("Access-Control-Expose-Headers", "xsrf-token");

			chain.doFilter(req, res);
		}

		@Override
		public void destroy() {
		}
	}
}