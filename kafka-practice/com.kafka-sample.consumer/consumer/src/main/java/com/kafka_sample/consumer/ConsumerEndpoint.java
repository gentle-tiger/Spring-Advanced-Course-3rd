package com.kafka_sample.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerEndpoint {
  // 토픽 동일, 그룹 다름 -> 같은 그룹(topic1)으로 묶여서 두 개의 컨슈머가 메시지를 소비한다.
  @KafkaListener(groupId = "group_a", topics = "topic1")
  public void consumerFromGroupA(String message){
    log.info("Group A consumed message from topic1: {}", message);
  }

  @KafkaListener(groupId = "group_b", topics = "topic1")
  public void consumerFromGroupB(String message){
    log.info("Group B consumed message from topic1: {}", message);
  }

  // 토픽 다름, 그룹 동일 -> 그룹 c의 컨슈머 리스너만 동작함을 볼 수 있음. (토픽을 대상으로 발생되기 때문에)
   @KafkaListener(groupId = "group_c", topics = "topic2")
  public void consumerFromGroupC(String message){
    log.info("Group C consumed message from topic2: {}", message);
   }

   @KafkaListener(groupId = "group_c" , topics = "topic3")
  public void consumerFromGroupD(String message){
    log.info("Group C consumed message from topic3: {}", message);
   }

   // 토픽 다름, 그룹 다름
   @KafkaListener(groupId = "group_d", topics = "topic4")
  public void consumerFromGroupE(String message){
    log.info("Group D consumed message from topic4: {}", message);
   }
}
