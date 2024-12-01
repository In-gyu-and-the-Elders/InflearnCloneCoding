package inflearn_clone.springboot.service.review;

import inflearn_clone.springboot.dto.review.ReviewListDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewListDTO> getReviewList(int courseIdx, String sortBy, int offset, int limit);
    int registerReview(ReviewListDTO review);
    int modifyReview(ReviewListDTO review);
    int deleteReview(int idx, String memberId);
    ReviewListDTO viewReview(int idx);

    int courseCntByTeacher(String memberId);
    int reviewCntByTeacher(String memberId);
    List<ReviewListDTO> reviewListByTeacher(int pageNo, int pageSize, String memberId);

    //리뷰평점
    Double avgRating(int courseIdx);
    // 리뷰 작성 여부 확인
    boolean writerCheck(String memberId, int courseIdx);
    // 리뷰 총 개수 조회
    int reviewCnt(int courseIdx);

}
