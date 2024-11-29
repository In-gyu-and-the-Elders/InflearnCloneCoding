package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.BbsVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeacherMapper {
    int lessonCount(@Param("teacherId") String teacherId, @Param("courseIdx") int courseIdx);
    List<CourseDTO> getMyCourseList(Map<String,Object> map);
    int sectionCount(@Param("teacherId") String teacherId, @Param("courseIdx") int courseIdx);
    int totalCount(@Param("teacherId") String teacherId, @Param("searchCategory") String searchCategory, @Param("searchValue") String searchValue);
}