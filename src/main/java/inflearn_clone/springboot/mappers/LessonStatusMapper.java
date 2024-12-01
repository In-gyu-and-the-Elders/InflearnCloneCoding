package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.dto.lessonStatus.LessonStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LessonStatusMapper {
    int countLessonStatus(@Param("lessonIdx") int lessonIdx, @Param("memberId") String memberId);
    int insertLessonStatus(@Param("lessonIdx") int lessonIdx, @Param("memberId") String memberId);
    int updateLessonStatus(@Param("lessonIdx") int lessonIdx, @Param("memberId") String memberId);
    int studyCheck(@Param("courseIdx") int courseIdx, @Param("memberId") String memberId);
}
