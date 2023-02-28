package jpabook.jpashop.api.controller;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  @GetMapping("api/v1/simple-orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAll(new OrderSearch());
    for (Order order : all) {
      order.getMember().getName(); //Lazy 강제 초기화 : Member에 쿼리를 날려서 실행됨
      order.getDelivery().getAddress(); //Lazy 강제 초기환
    }
    return all;
  }

  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {

    return orderRepository.findAll(new OrderSearch()).stream()
        .map(o -> new SimpleOrderDto(o)).collect(toList());
  }

  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    return orders.stream()
        .map(o -> new SimpleOrderDto(o)).collect(toList());
  }

  @GetMapping("/api/v4/simple-orders")
  public List<OrderSimpleQueryDto> ordersV4() {
    return orderRepository.findOrderDtos();
  }


  @Data
  static class SimpleOrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
    }
  }
}