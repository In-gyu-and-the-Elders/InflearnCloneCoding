package inflearn_clone.springboot.service.course;

import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.LessonVO;
import inflearn_clone.springboot.domain.SectionVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import org.apache.ibatis.annotations.Param;

import inflearn_clone.springboot.dto.course.CourseTotalDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface CourseSerivce {
    // 강의 목록 가져오기
    List<CourseDTO> courseList(String memberId);
    // 강의 상세 정보 가져오기

    CourseDTO courseView1(int idx);

    //아이디에 해당하는 강의 정보 확인하기(관리자 사용)
    List<CourseDTO> selectCourseByMemberId(String memberId);

    // 지금 필요 없음
    boolean deleteCourseByMemberId(String memberId);

    // 강좌 삭제(스케쥴러로 인해서 삭제)
    boolean updateCourseStatusToDeleted(LocalDateTime now);

    //강좌 삭제(클릭 시 삭제 될 것)
    boolean deleteCourese(String idx);

    //카테고리 별 강좌 수
    int categoryTotalCnt(String category);

    //전체 강좌 수
    int allCourseListTotalCnt(String searchCategory, String searchValue, String status);

    List<CourseDTO> allCourseList(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

    // 강의 상세 정보 가져오기
    CourseDTO courseView(int idx);

    List<CourseTotalDTO> getCourses(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);
    int getTotalCourses(String searchCategory, String searchValue);
    int insertCourse(CourseDTO courseDTO);
    CourseDTO viewMyLastCourse(String memberId);

    CourseDTO curriculum(int idx);
    CourseDTO selectCourse(int courseIdx);
    List<SectionDTO> selectSection(int courseIdx);
    List<LessonDTO> selectLesson(@Param("sectionIdx") List<Integer> sectionIdx);

    int updateCourse(CourseDTO courseDTO);
}
