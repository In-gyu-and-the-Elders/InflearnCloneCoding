package inflearn_clone.springboot.service.teacher;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.course.TeacherCourseDTO;

import java.util.List;

public interface TeacherServiceIf {
    List<TeacherCourseDTO> getMyCourse(String teacherId, int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

}
