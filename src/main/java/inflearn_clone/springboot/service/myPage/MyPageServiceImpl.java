package inflearn_clone.springboot.service.myPage;

import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;
import inflearn_clone.springboot.mappers.MyPageMapper;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final MyPageMapper myPageMapper;

    @Override
    public List<MyPageDTO> getMyCourses(String memberId, String sortType) {
        return myPageMapper.getMyCourses(memberId, sortType);
    }

    @Override
    public List<MyPageDTO> getLikedCourses(String memberId, String sortType) {
        return myPageMapper.getLikedCourses(memberId, sortType);
    }

    @Override
    public MemberDTO accountInfo(String memberId) {
        return myPageMapper.accountInfo(memberId);
    }

    @Override
    public void updateMemberInfo(MemberDTO memberDTO) {
        myPageMapper.updateMemberInfo(memberDTO);
    }

    @Override
    public List<MyPageDTO> getReview(String memberId) {
        return myPageMapper.getReview(memberId);
    }

    @Override
    public MyPageDTO getReviewById(int reviewIdx) {
        return myPageMapper.getReviewById(reviewIdx);
    }

    @Override
    public boolean updateReview(int reviewIdx, String memberId, String content, int rating) {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewIdx", reviewIdx);
        params.put("memberId", memberId);
        params.put("content", content);
        params.put("rating", rating);
        
        try {
            return myPageMapper.updateReview(params) > 0;
        } catch (Exception e) {
            log.error("리뷰 수정 중 오류 발생", e);
            return false;
        }
    }

    @Override
    public boolean deleteReview(int reviewIdx, String memberId) {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewIdx", reviewIdx);
        params.put("memberId", memberId);
        
        try {
            return myPageMapper.deleteReview(params) > 0;
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생", e);
            return false;
        }
    }

    @Override
    public void updateMemberLeave(String memberId, String leaveReason) {
        myPageMapper.updateMemberLeave(memberId, leaveReason);
    }
}