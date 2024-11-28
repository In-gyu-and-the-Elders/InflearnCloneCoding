package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.domain.SignVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SignMapper {
  SignVO signIn(String memberId, String memberType);
  void signUp(SignVO memberVO);
  SignVO getMemberInfo(String memberId);
  int modifyMemberInfo(SignVO memberVO);
  String findMemberId(String email);
  String findPassword(String email, String memberId);
  String deleteMember(String memberId);
  boolean checkDuplicateId(String memberId);
}
