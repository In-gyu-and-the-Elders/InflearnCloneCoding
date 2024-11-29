package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.ReviewVO;
import inflearn_clone.springboot.dto.review.ReviewListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {
    List<ReviewListDTO> getReviewList(@Param("courseIdx") int courseIdx,
                                      @Param("sortBy") String sortBy,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
    int insertReview(ReviewListDTO review);
    int modifyReview(ReviewListDTO review);
    int deleteReview(@Param("idx") int idx);
    ReviewListDTO viewReview(@Param("idx") int idx);

    int reviewCntByTeacher(@Param("memberId") String memberId);
    List<ReviewVO> reviewListByTeacher(Map<String, Object> map);
}
