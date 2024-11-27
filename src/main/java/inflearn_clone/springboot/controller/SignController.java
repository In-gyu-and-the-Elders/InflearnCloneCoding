package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.sign.SignDTO;
import inflearn_clone.springboot.service.sign.SignServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {

  private final SignServiceImpl signService;

  // 회원가입
  @PostMapping("/signUp")
  public ResponseEntity<String> signUp(@Valid @RequestBody SignDTO signDTO) {
    signService.signUp(signDTO);
    return ResponseEntity.ok("Signup successful!");
  }

  // 로그인
  @PostMapping("/signIn")
  public ResponseEntity<Map<String, String>> signIn(@Valid @RequestBody SignDTO signDTO) {
    log.info("Received SignDTO: {}", signDTO);

    String token = signService.signIn(signDTO.getMemberId(), signDTO.getPwd());
    return ResponseEntity.ok(Map.of("token", token));
  }

  // 회원 정보 조회
  @GetMapping("/member/{memberId}")
  public ResponseEntity<SignDTO> getMemberInfo(@PathVariable String memberId) {
    return ResponseEntity.ok(signService.getMemberInfo(memberId));
  }

  // 회원 정보 수정
  @PutMapping("/member")
  public ResponseEntity<String> modifyMemberInfo(@Valid @RequestBody SignDTO signDTO) {
    signService.modifyMemberInfo(signDTO);
    return ResponseEntity.ok("Member info updated successfully!");
  }

  // 회원 탈퇴
  @DeleteMapping("/member/{memberId}")
  public ResponseEntity<String> deleteMember(@PathVariable String memberId) {
    signService.deleteMember(memberId);
    return ResponseEntity.ok("Member deleted successfully!");
  }

  // 아이디 찾기
  @GetMapping("/find-member-id")
  public ResponseEntity<String> findMemberId(@RequestParam String email) {
    String memberId = signService.findMemberId(email);
    return ResponseEntity.ok(memberId);
  }

  // 비밀번호 찾기
  @GetMapping("/find-password")
  public ResponseEntity<String> findPassword(@RequestParam String memberId, @RequestParam String email) {
    String password = signService.findPassword(memberId, email);
    return ResponseEntity.ok(password);
  }

  // 아이디 중복 체크
  @GetMapping("/check-duplicate-id")
  public ResponseEntity<Map<String, Boolean>> checkDuplicateId(@RequestParam String memberId) {
    return ResponseEntity.ok(Map.of("available", signService.checkDuplicateId(memberId)));
  }
}