package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageMapper {
  List<MyPageDTO> getMyCourses(@Param("memberId") String memberId, @Param("sortType") String sortType);
  List<MyPageDTO> getLikedCourses(@Param("memberId") String memberId, @Param("sortType") String sortType);
  MemberDTO accountInfo(@Param("memberId") String memberId);
  void updateMemberInfo(MemberDTO memberDTO);
  List<MyPageDTO> getReview(String memberId);
  MyPageDTO getReviewById(@Param("reviewIdx") int reviewIdx);
  int updateReview(Map<String, Object> params);
  int deleteReview(Map<String, Object> params);
}
