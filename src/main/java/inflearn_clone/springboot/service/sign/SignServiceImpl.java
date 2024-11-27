package inflearn_clone.springboot.service.sign;

import inflearn_clone.springboot.config.JwtTokenProvider;
import inflearn_clone.springboot.domain.SignVO;
import inflearn_clone.springboot.dto.sign.SignDTO;
import inflearn_clone.springboot.mappers.SignMapper;
import inflearn_clone.springboot.utils.SignMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SignServiceImpl {

  private final SignMapper signMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  // 회원가입
  public void signUp(SignDTO signDTO) {
    // 비밀번호 암호화
    signDTO.setPwd(passwordEncoder.encode(signDTO.getPwd()));
    signMapper.signUp(SignMapperUtil.toVO(signDTO));
  }

  // 로그인
  public String signIn(String memberId, String rawPassword) {
    log.info("로그인 시도: memberId={}", memberId);

    // 1. 사용자 정보 조회
    SignVO signVO = signMapper.signIn(memberId);
    if (signVO == null) {
      log.error("로그인 실패: 존재하지 않는 사용자 ID - memberId=-{}-", memberId);
      throw new IllegalArgumentException("존재하지 않는 사용자 ID입니다.");
    }

    // 2. 비밀번호 검증
    if (!passwordEncoder.matches(rawPassword, signVO.getPwd())) {
      log.error("로그인 실패: 비밀번호 불일치 - memberId={}", memberId);
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    log.info("로그인 성공: memberId={}", memberId);

    // 3. JWT 토큰 생성 및 반환
    return jwtTokenProvider.createToken(memberId, signVO.getMemberType());
  }

  // 회원 정보 조회
  public SignDTO getMemberInfo(String memberId) {
    return SignMapperUtil.toDTO(signMapper.getMemberInfo(memberId));
  }

  // 회원 정보 수정
  public void modifyMemberInfo(SignDTO signDTO) {
    signMapper.modifyMemberInfo(SignMapperUtil.toVO(signDTO));
  }

  // 회원 탈퇴
  public void deleteMember(String memberId) {
    signMapper.deleteMember(memberId);
  }

  // 아이디 찾기
  public String findMemberId(String email) {
    String memberId = signMapper.findMemberId(email);
    if (memberId == null) {
      throw new IllegalArgumentException("없어");
    }
    return memberId;
  }

  // 비밀번호 찾기
  public String findPassword(String memberId, String email) {
    String password = signMapper.findPassword(memberId, email);
    if (password == null) {
      throw new IllegalArgumentException("없다고");
    }
    return password;
  }

  // 아이디 중복 체크
  public boolean checkDuplicateId(String memberId) {
    // true면 이미 존재하는 아이디, false면 사용 가능한 아이디
    boolean isDuplicate = signMapper.checkDuplicateId(memberId);
    
    return !isDuplicate;
  }
}
