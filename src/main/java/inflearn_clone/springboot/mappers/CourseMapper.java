package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    // 전체강좌 리스트
    List<CourseVO> courseList();
    // 강좌상세보기
    CourseVO courseView(int idx);
}
