package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  @NotEmpty
  private String name;
  @Embedded // 내장 타입이다! 라는 것을 알아볼 수 있음
  private Address address;
  @OneToMany(mappedBy = "member") // Order에 있는 member에 패핑된 거울일뿐
  private List<Order> orders = new ArrayList<>();
}
