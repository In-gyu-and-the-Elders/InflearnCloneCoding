package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.LessonVO;
import inflearn_clone.springboot.domain.SectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LessonMapper {
    int insertLesson(LessonVO lessonVO);
}
