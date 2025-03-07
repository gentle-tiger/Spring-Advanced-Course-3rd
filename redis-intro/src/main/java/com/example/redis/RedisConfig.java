package com.example.redis;

import com.example.redis.repository.ItemDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {


  @Bean
  public RedisTemplate<String, ItemDto> itemRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

    RedisTemplate<String, ItemDto> template = new RedisTemplate<>();

    template.setConnectionFactory(redisConnectionFactory); // 어떤 거와 연결할거냐?
    // string 이건 static이기 때문에 직접 만들 수도 있다...? 자바에 익숙해진다면?
    template.setKeySerializer(RedisSerializer.string()); // 키는 어떻게 직렬화, 역직렬화 할거냐
    template.setValueSerializer(RedisSerializer.json()); // 데이터 직렬화
    return template;
  }
}
