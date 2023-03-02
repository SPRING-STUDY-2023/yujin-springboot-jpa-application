package jpabook.jpashop.api.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @GetMapping("/api/v1/members")
  public Result membersV1() {
    List<Member> findMembers = memberService.findMembers();
    List<MemberDTO> collect = findMembers.stream()
        .map(m -> new MemberDTO(m.getName()))
        .toList();
    return new Result(collect.size(), collect);
  }

  @Data
  @AllArgsConstructor
  static class Result<T> {
    private int count;
    private T data;
  }

  @Data
  @AllArgsConstructor
  static class  MemberDTO {
    private String name;
  }

  @PostMapping("/api/v1/members")
  public CreateMemberResponse saveMember(@RequestBody @Valid Member member) {
    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  @PostMapping("/api/v2/members")
  public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {
    Member member = new Member();
    member.setName(request.getName());

    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  /**
   * 수정 API
   */
  @PutMapping("/api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
      @RequestBody @Valid UpdateMemberRequest request) {
    memberService.update(id, request.getName());
    Member findMember = memberService.findOne(id);
    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
  }
  @Data
  static class UpdateMemberRequest {
    private String name;
  }
  @Data
  @AllArgsConstructor
  static class UpdateMemberResponse {
    private Long id;
    private String name;
  }

  @Data
  static class CreateMemberRequest {
    private String name;
  }

  @Data
  static class CreateMemberResponse {
    private Long id;
    public CreateMemberResponse(Long id) {
      this.id = id;
    }
  }

}
