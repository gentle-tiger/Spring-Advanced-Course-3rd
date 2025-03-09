package com.example.redis.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {
  CacheManager cacheManager;


  @Bean
  public CacheManager cacheManager(
    RedisConnectionFactory redisConnectionFactory
  ){
    // 1. 설정 구성을 먼저 진행한다
    // Redis를 이용해서 Spring Cache를 사용할 때 redis관련 설정을 모아두는 클래스
    RedisCacheConfiguration configuration = RedisCacheConfiguration
        .defaultCacheConfig()
        // null을 캐싱하는지
        .disableCachingNullValues()
        // 기본 캐시 유지 시간(Time of Live)
        .entryTtl(Duration.ofSeconds(100))
        // 캐시를 구분하는 접두사 설정
        .computePrefixWith(CacheKeyPrefix.simple())
        // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할 것인지. java Serializer 사용.
        .serializeValuesWith(
            SerializationPair.fromSerializer(RedisSerializer.java())
        );

    return RedisCacheManager
        .builder(redisConnectionFactory)
        .cacheDefaults(configuration)
        .build();
  }
}
