package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.LessonVO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionWithLessonListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LessonMapper {
    int insertLesson(LessonVO lessonVO);
    List<LessonVO> getLessons(@Param("sectionIdx") int sectionIdx);
    int updateLesson(LessonVO lessonVO);
  
    //동영상페이지
    LessonVO getLessonById(int lessonIdx);
    int getCourseIdxByLessonId(int lessonIdx);
    LessonVO getPreviousLesson(int lessonIdx);
    LessonVO getNextLesson(int lessonIdx);
    String getVideo(@Param("lessonIdx") int lessonIdx);
}
