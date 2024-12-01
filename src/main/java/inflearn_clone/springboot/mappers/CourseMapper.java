package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.LessonVO;
import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.domain.SectionVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.course.CourseTotalDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper {
    // 전체강좌 리스트
    List<CourseVO> courseList(Map<String, Object> map);

    CourseVO courseView1(int idx);

    // 특정 teacherId로 존재하는 강의가 있는지 확인하기
    List<CourseVO> selectCourseByMemberId(String teacherId);

    // 특정 teacherId로 강좌 삭제
    boolean deleteCourseByMemberId(String teacherId);

    //삭제 날짜 정함
    boolean updateDeleteDate(@Param("idx") Integer idx, @Param("deleteDate") LocalDateTime deleteDate);

    //삭제 항목에 포함되는 강의 삭제
    int deleteCoursesByIdx(@Param("list") List<Integer> idx);

    int updateCourseStatusToDeleted(@Param("now") LocalDateTime now);

    List<Integer> findCoursesToDeleted(@Param("now") LocalDateTime now);

    boolean deleteCourse(String idx);

    //강좌 카테고리 별 조회
    int categoryTotalCnt(String category);

    //강좌 조회
    int allCourseListTotalCnt(String searchCategory, String searchValue, String status);

    List<CourseVO> allCourseList(Map<String, Object> map);

    // 강좌상세보기
    CourseTotalDTO courseView(int idx);
    // 페이징 버전
    List<CourseTotalDTO> selectAllCourse(Map<String, Object> map);
    int courseTotalCnt(@Param("categoryCodes") List<String> categoryCodes, @Param("searchValue") String searchValue);

    int insertCourse(CourseVO courseVO);
    CourseVO viewMyLastCourse(@Param("memberId") String memberId);
    void finishInsert(@Param("courseIdx") int courseIdx);

    int updateCourse(CourseVO courseVO);

    CourseVO selectCourse(int idx);
    List<SectionVO> selectSection(int idx);
    List<LessonVO> selectLesson(@Param("sectionIdx") List<Integer> sectionIdx);

    boolean processDeleteRequest(@Param("courseIdx") long courseIdx);
    boolean processDeleteRequestN(@Param("courseIdx") long courseIdx);

    int courseTotalCnt(Map<String, Object> map);
}
