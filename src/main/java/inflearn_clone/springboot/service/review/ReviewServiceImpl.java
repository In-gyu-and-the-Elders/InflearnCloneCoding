package inflearn_clone.springboot.service.review;

import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.mappers.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
