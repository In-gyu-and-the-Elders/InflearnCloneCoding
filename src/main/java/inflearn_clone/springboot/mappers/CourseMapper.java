package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseVO> courseList();
    CourseVO courseView(int idx);

    // 특정 teacherId로 존재하는 강의가 있는지 확인하기
    List<Integer> selectCourseByMemberId(String teacherId);

    // 특정 teacherId로 강좌 삭제
    boolean deleteCourseByMemberId(String teacherId);

    //삭제 날짜 정함
    boolean updateDeleteDate(@Param("idx") Integer idx, @Param("deleteDate") LocalDateTime deleteDate);

    //삭제 해야 할 강의 idx 가져옴
    List<Integer> findCourseToDelete(@Param("now") LocalDateTime now);

    //삭제 항목에 포함되는 강의 삭제
    int deleteCoursesByIdx(@Param("list") List<Integer> idx);

    boolean updateCourseStatusToDeleted(@Param("now") LocalDateTime now);


 }
