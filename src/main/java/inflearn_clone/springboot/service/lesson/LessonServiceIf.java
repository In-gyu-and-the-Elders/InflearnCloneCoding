package inflearn_clone.springboot.service.lesson;

import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;

import java.util.List;
import java.util.Map;

public interface LessonServiceIf {
    int insertLesson(LessonDTO lessonDTO);
    List<LessonDTO> getLessons(int sectionIdx);
    int updateLesson(LessonDTO lessonDTO);

    //동영상페이지
    LessonDTO getLessonById(int lessonIdx);
    int getCourseIdxByLessonId(int lessonIdx);
    LessonDTO getPreviousLesson(int lessonIdx);
    LessonDTO getNextLesson(int lessonIdx);
}
