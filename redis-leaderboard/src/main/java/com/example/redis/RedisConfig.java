package com.example.redis;

import com.example.redis.domain.ItemDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, ItemDto> rankTemplate(
      RedisConnectionFactory redisConnectionFactory
  ){
    // ItemDto가 아니라면 다른 걸 넣어도 됨. 단 ItemDto를 넣으면 추가작업없이 실행되므로 넣었음.
    RedisTemplate<String, ItemDto> template= new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        return template;
  }
}
