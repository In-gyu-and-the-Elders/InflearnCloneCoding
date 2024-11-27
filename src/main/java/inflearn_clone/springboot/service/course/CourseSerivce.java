package inflearn_clone.springboot.service.course;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.utils.Paging;

import java.util.List;

public interface CourseSerivce {
    // 강의 목록 가져오기 ( 페이징 x)
    List<CourseDTO> courseList();
    // 강의 상세 정보 가져오기
    CourseDTO courseView(int idx);

    List<CourseDTO> getCourses(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);
    int getTotalCourses(String searchCategory, String searchValue);
}
