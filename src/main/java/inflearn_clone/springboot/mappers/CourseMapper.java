package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseVO> courseList();
    CourseVO courseView(int idx);

    // 특정 teacherId로 존재하는 강의가 있는지 확인하기
    List<Integer> selectCourseByMemberId(String teacherId);

    // 특정 teacherId로 강좌 삭제
    boolean deleteCourseByMemberId(String teacherId);



 }
