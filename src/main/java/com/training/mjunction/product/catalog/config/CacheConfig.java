package com.training.mjunction.product.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@EnableCaching
public class CacheConfig {

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(@Value("${redis.host:localhost}") final String host,
			@Value("${redis port:6379") final int port) {
		return new JedisConnectionFactory(new RedisStandaloneConfiguration(host, port));
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(final JedisConnectionFactory jedisConnectionFactor) {
		final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactor);
		redisTemplate.setExposeConnection(true);
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager(final JedisConnectionFactory jedisConnectionFactory) {
		return RedisCacheManager.builder(jedisConnectionFactory).disableCreateOnMissingCache().transactionAware()
				.build();
	}

}
