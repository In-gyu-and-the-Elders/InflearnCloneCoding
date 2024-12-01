package inflearn_clone.springboot.service.teacher;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;

import java.util.List;
import inflearn_clone.springboot.dto.course.TeacherCourseDTO;

import java.util.List;

public interface TeacherServiceIf {
    //선생님 수
    int teacherTotalCnt(String searchCategory, String searchValue, String memberType, String status);

    //선생님 목록
    List<MemberDTO> selectAllTeacher(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery, String memberType, String status);
    List<TeacherCourseDTO> getMyCourse(String teacherId, int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

}
