package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jpabook.jpashop.domain.Item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected 으로 생성자 생성, 다른 곳에서 생성자 못생성하게막는다
public class OrderItem {
  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item; //주문 상품

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order; //주문
  private int orderPrice; //주문 가격
  private int count; //주문 수량

  /* 생성 메서드 */
  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setOrderPrice(orderPrice);
    orderItem.setCount(count);

    item.removeStock(count);
    return orderItem;
  }
  /* 비즈니스 로직 */
  public void cancel() {
    getItem().addStock(count);
  }

  // 주문 상품 전체 가격 조회
  public int getTotalPrice() {
    return getOrderPrice() * getCount();
  }
}

