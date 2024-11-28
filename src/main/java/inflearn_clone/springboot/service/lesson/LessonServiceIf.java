package inflearn_clone.springboot.service.lesson;

import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;

import java.util.List;

public interface LessonServiceIf {
    int insertLesson(LessonDTO lessonDTO);
}
