package com.example.redis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("orders")
public class OrderController {

  private final OrderRepository orderRepository;
  public OrderController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @PostMapping
  public ItemOrder create(@RequestBody ItemOrder order) {

    return orderRepository.save(order);
  }

  @GetMapping
  public List<ItemOrder> readAll(){
    List<ItemOrder> orders = new ArrayList<>();
    orderRepository.findAll()
        .forEach(orders::add);

  return orders;
  }

  @GetMapping("{id}")
  public ItemOrder readOne(@PathVariable("id") String id) {
    return orderRepository.findById(id)
        .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @PutMapping("{id}")
  public ItemOrder update(@PathVariable("id") String id, @RequestBody ItemOrder order) {
    ItemOrder target = orderRepository
        .findById(id)
        .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    target.setItem(order.getItem());
    target.setCount(order.getCount());
    target.setTotalPrice(order.getTotalPrice());
    target.setStatus(order.getStatus());
    return orderRepository.save(target);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") String id) {
    orderRepository.deleteById(id);
  }
}

