package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper {
    List<CourseVO> courseList();
    CourseVO courseView1(int idx);

    // 특정 teacherId로 존재하는 강의가 있는지 확인하기
    List<Integer> selectCourseByMemberId(String teacherId);

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





 }
