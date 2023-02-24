package jpabook.jpashop.api.controller;

import java.util.List;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
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
}
