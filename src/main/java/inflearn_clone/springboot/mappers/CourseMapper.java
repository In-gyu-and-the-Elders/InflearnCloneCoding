package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseVO> courseList();
    CourseVO courseView(int idx);
    int insertCourse(CourseVO courseVO);
    CourseVO viewMyLastCourse(@Param("memberId") String memberId);
}
