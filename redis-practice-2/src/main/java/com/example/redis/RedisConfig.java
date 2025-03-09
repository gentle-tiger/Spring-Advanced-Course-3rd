package com.example.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfig {
  @Bean
  public RedisTemplate<String, Integer> articleTemplate(
      RedisConnectionFactory redisConnectionFactory
  ){
    RedisTemplate<String, Integer> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

    return template;
  }
}

// GenericToStringSerializer 문자열을 정수로 변환해줌. 직렬화