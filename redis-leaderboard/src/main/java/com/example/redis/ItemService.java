package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDto;
import com.example.redis.domain.ItemOrder;
import com.example.redis.repository.ItemRepository;
import com.example.redis.repository.OrderRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ZSetOperations<String, ItemDto> rankOps;
    public ItemService(
            ItemRepository itemRepository,
            OrderRepository orderRepository,
        RedisTemplate<String, ItemDto> rankTemplate
        // rankOps의 타입이 ZSetOperations가 되고 redis의 SortedSet의 커맨드들이 메서드회 된 것임.
    ) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.rankOps = rankTemplate.opsForZSet();
    }

    public void purchase(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        orderRepository.save(ItemOrder.builder()
                .item(item)
                .count(1)
                .build());
//        ItemDto dto = ItemDto.fromEntity(item);
        // dto 가 존재하지 않으면 자동으로 만들어준다...
        rankOps.incrementScore("soldRanks", ItemDto.fromEntity(item), 1);
//        rankOps.incrementScore("soldRanks", dto, 1);
        // ZADD : 이건 게임 랭크를 할 때 Readerboard 에 따라 다른 것!!!

    }

    public List<ItemDto> getMostSold(){
        Set<ItemDto> ranks = rankOps.reverseRange("soldRanks", 0 , 9);
        if(ranks == null) return Collections.emptyList();

        return ranks.stream().toList();
    }



}
