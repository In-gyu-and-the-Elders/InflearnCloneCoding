package inflearn_clone.springboot.service.review;

import inflearn_clone.springboot.dto.review.ReviewListDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewListDTO> getReviewList(int courseIdx, String sortBy, int page);
    int registerReview(ReviewListDTO review);
    int modifyReview(ReviewListDTO review);
    int deleteReview(int idx);
    ReviewListDTO viewReview(int idx);

    int reviewCntByTeacher(String memberId);
    List<ReviewListDTO> reviewListByTeacher(int pageNo, int pageSize, String memberId);
}
