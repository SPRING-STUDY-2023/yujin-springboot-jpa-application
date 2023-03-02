package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
// 화면에 종속된 조회전용으로 보이게끔
// 원하는 부분만 select 하는 것
// 재사용성이 없음
// API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점
public class OrderSimpleQueryRepository {
  private final EntityManager em;
  public List<OrderSimpleQueryDto> findOrderDtos() {
    return em.createQuery(
        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
            + " from Order o" +
            " join o.member m" +
            " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
  }
}