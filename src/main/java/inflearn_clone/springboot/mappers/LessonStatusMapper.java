package inflearn_clone.springboot.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LessonStatusMapper {
    int countLessonStatus(@Param("lessonIdx") int lessonIdx, @Param("memberId") String memberId);
    int insertLessonStatus(@Param("lessonIdx") int lessonIdx, @Param("memberId") String memberId);
    int updateLessonStatus(@Param("lessonIdx") int lessonIdx, @Param("memberId") String memberId);
}
