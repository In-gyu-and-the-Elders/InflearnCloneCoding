package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.domain.SignVO;
import org.apache.ibatis.annotations.Select;

public interface SignMapper {
  SignVO login(String memberId);
  SignVO loginAdmin(String memberId, String memType);
  SignVO viewMember(String memberId);
  int registMember(SignVO memberVO);
  String pwdCheck(String memberId);
  int modifyMember(SignVO memberVO);
  String memberIdCheck(String memberId);
}
