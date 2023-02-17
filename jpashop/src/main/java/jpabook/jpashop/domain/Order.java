package jpabook.jpashop.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member; //주문 회원
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

//  Cascade가 없다면,각각 다 저장을 해줘야했다.
//  em.persist(orderItemA); em.persist(orderItemB); em.persist(orderItemC);
//  얘를 대신해서 em.persist(order)만 해도 orderItem의 정보를 다 저장해줌

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery; //배송정보
  private LocalDateTime orderDate; //주문시간 @Enumerated(EnumType.STRING)
  private OrderStatus status; //주문상태 [ORDER, CANCEL]

  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder((this));
  }

  public void setDeliverty(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }
}


