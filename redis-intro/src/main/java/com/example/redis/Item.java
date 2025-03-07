package com.example.redis;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Entity 대신
@RedisHash("item")
public class Item implements Serializable {
  @Id
  // Id String으로 쓰면 UUID가 자동으로 배정된다.
  private String id;
  private String name;
  private String description;
  private Integer price;
}
