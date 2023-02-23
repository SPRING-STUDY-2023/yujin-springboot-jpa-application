package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final EntityManager em;
  public void save(Item item) {
    if (item.getId() == null) {
      em.persist(item); // id 값이 없는 거는 아직 영속성 컨텍스트에만 있고 DB에는 없는 상태
    } else {
      em.merge(item);
//      Item merge = em.merge(item); merge가 영속성에서 관리 되는 것
//      merge는 완전 갈아엎는 방법. 만약에 price값을 따로 set하지 않는다면, null로 들어가게됨
//      따로 수정할 부분들만 set해서 바꿔주는 것이 좋음
    }
  }

  public Item findOne(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class).getResultList();
  }

}
