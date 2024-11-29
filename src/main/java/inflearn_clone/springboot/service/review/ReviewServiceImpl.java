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
    public List<ReviewListDTO> getReviewList(int courseIdx, String sortBy, int page) {
        int limit = 5;
        int offset = page * limit;

        log.info("getReviewList 호출: courseIdx={}, sortBy={}, page={}, offset={}, limit={}",
                courseIdx, sortBy, page, offset, limit);

        List<ReviewListDTO> reviews = reviewMapper.getReviewList(courseIdx, sortBy, offset, limit);

//        log.info("조회된 리뷰 수: {}", reviews.size());
        return reviews;
    }

    @Override
    public int registerReview(ReviewListDTO review) {
        return reviewMapper.insertReview(review);
    }

    @Override
    public int modifyReview(ReviewListDTO review) {
        return reviewMapper.modifyReview(review);
    }

    @Override
    public int deleteReview(int idx) {
        return reviewMapper.deleteReview(idx);
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
}
