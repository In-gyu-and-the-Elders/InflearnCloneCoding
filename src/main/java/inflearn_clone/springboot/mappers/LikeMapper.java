package inflearn_clone.springboot.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
    // 좋아요 추가
    int regist(@Param("courseIdx") int courseIdx, @Param("memberId") String memberId);

    // 좋아요 삭제
    int delete(@Param("courseIdx") int courseIdx, @Param("memberId") String memberId);

    // 특정 강의의 좋아요 수 조회
    int likeCnt(@Param("courseIdx") int courseIdx);

    // 특정 회원이 특정 강의를 좋아요 눌렀는지 확인
    int likeCheck(@Param("courseIdx") int courseIdx, @Param("memberId") String memberId);
}
