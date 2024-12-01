package inflearn_clone.springboot.service.review;

import inflearn_clone.springboot.domain.BbsVO;
import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.domain.ReviewVO;
import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.mappers.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.exceptions.PersistenceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ModelMapper modelMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewListDTO> getReviewList(int courseIdx, String sortBy, int offset, int limit) {
        return reviewMapper.getReviewList(courseIdx, sortBy, offset, limit);
    }

    @Override
    public int registerReview(ReviewListDTO review) {
        return reviewMapper.insertReview(review);
    }

    @Override
    public int modifyReview(ReviewListDTO review) {
        try {
            // 수정 쿼리 실행
            return reviewMapper.modifyReview(review);
        } catch (PersistenceException e) {
            log.error("MyBatis 오류 발생: ", e);
            throw new RuntimeException("리뷰 수정 실패", e); // 필요한 경우 RuntimeException을 던짐
        } catch (Exception e) {
            log.error("리뷰 수정 중 알 수 없는 오류 발생: ", e);
            return 0; // 실패 시 0 반환
        }
    }

    @Override
    public int deleteReview(int idx, String memberId) {
        return reviewMapper.deleteReview(idx, memberId);
    }

    @Override
    public ReviewListDTO viewReview(int idx) {
        return reviewMapper.viewReview(idx);
    }

    @Override
    public int courseCntByTeacher(String memberId) {
        return reviewMapper.courseCntByTeacher(memberId);
    }

    @Override
    public int reviewCntByTeacher(String memberId) {
        return reviewMapper.reviewCntByTeacher(memberId);
    }

    @Override
    public List<ReviewListDTO> reviewListByTeacher(int pageNo, int pageSize, String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("memberId", memberId);
        List<ReviewVO> voList =  reviewMapper.reviewListByTeacher(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, ReviewListDTO.class)).collect(Collectors.toList());

    }
    // 리뷰평점
    @Override
    public Double avgRating(int courseIdx) {
        return reviewMapper.avgRating(courseIdx);
    }

    @Override
    public boolean writerCheck(String memberId, int courseIdx) {
        int result = reviewMapper.writerCheck(memberId, courseIdx);
        return result > 0;
    }

    @Override
    public int reviewCnt(int courseIdx) {
        return reviewMapper.reviewcnt(courseIdx);
    }
}
