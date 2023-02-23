package jpabook.jpashop.domain.Item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
  @Id @GeneratedValue
  @Column(name = "item_id")
  private Long id;
  private String name;
  private int price;
  private int stockQuantity;
  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<Category>();

  /*  재고 증가 */
  // 데이터를 가지고 있는 쪽에서, 비즈니스 메소드가 있는 것이 응집력이 있다.
  // 핵심 메서드를 가지고 데이터를 변경하는 것! set을 사용X
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }

  /*  재고 감소 */
  public void removeStock(int quantity) {
    int restStock = this.stockQuantity - quantity;
    if(restStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }
    this.stockQuantity = restStock;
  }
}
