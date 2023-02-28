package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;

  public void save(Order order) {
    em.persist(order);
  }

  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }

  public List<Order> findAll(OrderSearch orderSearch) {

    String jpql = "select o From Order o join o.member m";
    boolean isFirstCondition = true;
//주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " o.status = :status";
    }
//회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " m.name like :name";
    }
    TypedQuery<Order> query = em.createQuery(jpql, Order.class) .setMaxResults(1000); //최대 1000건
    if (orderSearch.getOrderStatus() != null) {
      query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      query = query.setParameter("name", orderSearch.getMemberName());
    }
    return query.getResultList();

//    return em.createQuery("select o from Order o join o.member m"
//         + " where o.status = :status " +
//        " and m.name like :name", Order.class)
//        .setParameter("status", orderSearch.getOrderStatus())
//        .setParameter("name", orderSearch.getMemberName())
//        .setMaxResults(1000)
//        .getResultList();
  }
  public List<Order> findAllByCriteria(OrderSearch orderSearch) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> o = cq.from(Order.class);
    Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
    List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      Predicate status = cb.equal(o.get("status"),
          orderSearch.getOrderStatus());
      criteria.add(status);
    }
//회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      Predicate name =
          cb.like(m.<String>get("name"), "%" +
              orderSearch.getMemberName() + "%");
      criteria.add(name);
    }
    cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
    TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
    return query.getResultList();
  }

  // 재사용성이 높음
  public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
        "select o from Order o"
            + " join fetch o.member m"
            + " join fetch o.delivery d",  Order.class
    ).getResultList();
  }

  // 원하는 부분만 select 하는 것
  // 재사용성이 없음
  // API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점
  public List<OrderSimpleQueryDto> findOrderDtos() {
    return em.createQuery(
        "select new jpabook.jpashop.repository."
            + "OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
        " from Order o" +
            " join o.member m" +
            " join o.delivery d", OrderSimpleQueryDto.class)
    .getResultList();
  }
}
