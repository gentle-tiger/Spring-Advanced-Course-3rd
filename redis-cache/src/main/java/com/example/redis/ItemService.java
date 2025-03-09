package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDto;
import com.example.redis.repo.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // write-through 방식
    // create 이후 생성된 데이터를 캐시가 사라지기 전에 조회하면 그 메서드가 db를 바라보지 않고 cache를 보고 데이터를 조회함.
    @CachePut(cacheNames = "itemCache", key = "#result.id")
    public ItemDto create(ItemDto dto) {
        return ItemDto.fromEntity(itemRepository.save(Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build()));
    }

    @Cacheable(cacheNames = "itemAllCache", key = "methodName")
    public List<ItemDto> readAll() {
        return itemRepository.findAll()
                .stream() // 'findAll()'의 결과를 스트림으로 변환
                .map(ItemDto::fromEntity) // 각 'Item' 엔티티를 'ItemDto'로 변환
                .toList(); // 변환된 객체들을 리스트로 수집하여 반환
    }
    // 이 메서드의 결과는 캐싱이 가능하다
    // cacheNames : 적용할 캐시 규칙을 지정하기 위한 이름. 이 메서드로 인해서 만들어질 캐시를 지칭하는 이름.
    // key : 캐시 데이터를 구분하기 위해 활용하는 값
    // Cache Aside 방식
    @Cacheable(cacheNames = "itemCache", key = "#id")
    public ItemDto readOne(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // item 의 정보가 바뀌었기 떄문에 이전 캐시는 제거한 후 변경한 정보를 캐시가 가지고 있게 한다. 이렇게 하면 readOne도 더 빠르게 조회가 가능하다.
    @CachePut(cacheNames = "itemCache", key = "#id") // key에 해당하는 id를 가진 데이터는 캐시 저장
    @CacheEvict(cacheNames = "itemAllCache", allEntries = true) // itemAllCache 캐시를 모두 제거
//    @CacheEvict(cacheNames = "itemAllCache", key = "'readAll'") // itemAllCache 캐시를 모두 제거
    public ItemDto update(Long id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDto.fromEntity(itemRepository.save(item));
    }

//    @CacheEvict(cacheNames = {"itemCache", "itemAllCache"}, key = "#id" , allEntries = true)
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Cacheable(
        cacheNames = "itemSearchCache",
        key = "{ #query, #pageable.pageNumber, #pageable.pageSize }"
    )
    public Page<ItemDto> searchByName(String query, Pageable pageable) {
        return itemRepository.findAllByNameContains(query,pageable)
            .map(ItemDto::fromEntity);
    }
}
