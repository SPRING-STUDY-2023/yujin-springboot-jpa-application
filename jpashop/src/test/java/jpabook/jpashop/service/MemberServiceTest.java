package jpabook.jpashop.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
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
public class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;
  @Autowired EntityManager em;

  @Test
  @DisplayName("회원가입")
//  @Rollback(value = false) // DB 롤백 취소
  public void 회원가입() throws Exception {
      // given
      Member member = new Member();
      member.setName("kim");
      
      // when
    Long saveId = memberService.join(member); // insert 문이 안나감
    Member findMember=  memberRepository.findOne(saveId);
      
      // then
//    em.flush(); DB에 쿼리가 강제로 나감
    assertThat(findMember.getId(),is(equalTo(member.getId())));
  }

  @Test
  @DisplayName("중복_회원_예외")
  void 중복_회원_예외() throws Exception{
      // given
    Member member1 = new Member();
    member1.setName("kim");

    Member member2 = new Member();
    member2.setName("kim");

      // when
    memberService.join(member1);
    try {
      memberService.join(member2);
    } catch (IllegalStateException e) {
      return;
    }
      // then
  }
}