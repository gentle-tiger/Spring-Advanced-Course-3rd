package com.example.redis;

import com.example.redis.repository.ItemDto;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTemplateTests {

  // RedisTemplate을 사용해 문자열 연산을 수행할 StringRedisTemplate을 자동 주입
  // StringRedisTemplate은 Redis의 문자열 데이터를 처리하는 데 특화된 템플릿입니다.
  // Redis에서 'String' 타입 데이터를 다룰 때 편리하게 사용됩니다.
  @Autowired
  StringRedisTemplate stringRedisTemplate;

  @Test
  public void stringOpsTest() {
    // ValueOperations<String, String>은 Redis에서 문자열을 다룰 때 사용되는 클래스입니다.
    // 여기서 'String, String'은 Redis 키와 값의 데이터 타입이 모두 String임을 의미합니다.
    // 즉, Redis에서 키와 값을 문자열로 처리할 수 있도록 도와주는 객체입니다.
    // Redis에서 'String'은 단일 값(키-값 쌍)을 저장하는 데 사용됩니다.
    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

    // ops.set("simplekey", "simplevalue")는 Redis에 "simplekey"라는 키로 "simplevalue"라는 값을 저장하는 작업입니다.
    // Redis에서는 set 명령어를 사용하여 키와 값을 저장하는 방식입니다.
    ops.set("simplekey", "simplevalue");

    // ops.get("simplekey")는 "simplekey" 키에 해당하는 값을 Redis에서 조회하는 작업입니다.
    // Redis에서는 get 명령어를 사용하여 키에 대한 값을 반환합니다.
    System.out.println(ops.get("simplekey")); // 출력: simplevalue

    // SetOperations<String, String>은 Redis에서 집합(Set) 자료형을 다룰 때 사용되는 클래스입니다.
    // Redis의 집합은 중복을 허용하지 않으며, 순서가 없는 데이터를 저장하는 자료형입니다.
    // Set은 집합론에서 말하는 집합 개념과 유사하게 동작합니다.
    // 예를 들어, 한 사용자가 여러 개의 취미를 가질 수 있지만, 중복된 취미는 허용하지 않습니다.
    SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();

    // setOps.add("hobbies", "games")는 "hobbies"라는 집합에 "games"라는 값을 추가하는 작업입니다.
    // Redis의 집합에서는 중복된 값을 허용하지 않으므로 "games"는 한 번만 저장됩니다.
    setOps.add("hobbies", "games");

    // setOps.add("hobbies", "coding", "alcohol", "games")는 "hobbies" 집합에 여러 값을 한 번에 추가하는 작업입니다.
    // "games"는 이미 집합에 존재하므로 추가되지 않습니다.
    setOps.add("hobbies", "coding", "alcohol", "games");

    // stringRedisTemplate.expire("hobbies", 3, TimeUnit.SECONDS)로 "hobbies" 집합의 만료 시간을 설정합니다.
    // 이 명령은 3초 후에 "hobbies" 집합을 Redis에서 자동으로 삭제하도록 설정합니다.
    // Redis는 만료 시간이 지난 키를 자동으로 삭제하는 기능을 제공합니다.
    // 만료 시간이 지난 데이터는 "expire" 명령어로 삭제됩니다.
    stringRedisTemplate.expire("hobbies", 3, TimeUnit.SECONDS);

    // stringRedisTemplate.delete("simplekey")는 Redis에서 "simplekey" 키를 삭제하는 작업입니다.
    // Redis에서는 delete 명령어를 사용하여 데이터를 삭제할 수 있습니다.
    // 삭제된 키는 Redis에서 더 이상 존재하지 않으며, 이 후에 해당 키를 조회하려 하면 null을 반환합니다.
    stringRedisTemplate.delete("simplekey");
  }

  @Autowired
  private RedisTemplate<String, ItemDto> itemRedisTemplate;

  @Test
  public void itemRedisTemplateTest() {
    ValueOperations<String, ItemDto> ops
        = itemRedisTemplate.opsForValue();

    ops.set("my:keyboard", ItemDto.builder()
        .name("Mechanical Keyboard")
        .price(250000)
        .description("OMG")
        .build()
    );
    System.out.println(ops.get("my:keyboard"));

    ops.set("my:mouse", ItemDto.builder()
        .name("mouse mice")
        .price(20000)
        .description("OMG")
        .build()
    );
    System.out.println(ops.get("my:mouse"));

  }

}
