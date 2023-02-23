package jpabook.jpashop.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // Rollback해줌
class OrderServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderRepository orderRepository;

  @Test
  @DisplayName("")
  void 상품주문() throws Exception {
    // given
    Member member = getMember("회원1");

    Item book = getBook(10000, "JPA", 10);

    int orderCount = 2;

    // when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals(OrderStatus.ORDER, getOrder.getStatus());
    assertEquals(1, getOrder.getOrderItems().size());
    assertEquals(10000 * orderCount, getOrder.getTotalPrice());
    assertEquals(8, book.getStockQuantity());
//    assertThat(findMember.getId(),is(equalTo(member.getId())));

  }

  private Member getMember(String 회원1) {
    Member member = new Member();
    member.setName(회원1);
    member.setAddress(new Address("서울", "강가", "123-123"));
    em.persist(member);
    return member;
  }

  private Item getBook(int price, String jpa, int stockQuantity) {
    Item book = new Book();
    book.setName(jpa);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  @Test
  void 상품주문_재고수량초과시() throws Exception {
    // given
    Member member = getMember("회원1");
    Item book = getBook(10000, "JPA", 10);

    int orderCount = 11;
    // when

    assertThrows(NotEnoughStockException.class, () -> {
      orderService.order(member.getId(), book.getId(), orderCount);
    }, "NotEnoughStockException is accrued");
    // then
  }
  @Test
  void 주문취소() throws Exception {
      // given
    Member member = getMember("회원1");
    Item book = getBook(10000, "JPA", 10);

    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
      // when
    orderService.cancelOrder(orderId);
      // then
    Order order = orderRepository.findOne(orderId);
    assertEquals(OrderStatus.CANCEL, order.getStatus());
    assertEquals(10, book.getStockQuantity());
  }
}