package inflearn_clone.springboot.service.myPage;

import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;

import java.util.List;

public interface MyPageService {
    List<MyPageDTO> getMyCourses(String memberId, String sortType);
    List<MyPageDTO> getLikedCourses(String memberId, String sortType);
    MemberDTO accountInfo(String memberId);
    void updateMemberInfo(MemberDTO memberDTO);
    List<MyPageDTO> getReview(String memberId);
    MyPageDTO getReviewById(int reviewIdx);
    boolean updateReview(int reviewIdx, String memberId, String content, int rating);
    boolean deleteReview(int reviewIdx, String memberId);
    void updateMemberLeave(String memberId, String leaveReason);
}
