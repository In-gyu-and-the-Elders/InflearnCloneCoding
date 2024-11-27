package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper {
    // 전체강좌 리스트
    List<CourseVO> courseList();
    // 강좌상세보기
    CourseVO courseView(int idx);
    // 페이징 버전
    List<CourseVO> selectAllCourse(Map<String, Object> map);
    int courseTotalCnt(@Param("searchCategory") String searchCategory, @Param("searchValue") String searchValue);
}
