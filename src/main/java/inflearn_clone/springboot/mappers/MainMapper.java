package inflearn_clone.springboot.mappers;

import org.apache.ibatis.annotations.Mapper;

import inflearn_clone.springboot.dto.main.MainDTO;

import java.util.List;

@Mapper
public interface MainMapper {
    // 최신 강의 목록
    List<MainDTO> getRecentCourses();
    
    // 카테고리별 강의 목록
    List<MainDTO> getCoursesByCategory(String category);
    
    // 인기 강의 목록 (나중에 주문 테이블과 연동하여 구현)
    List<MainDTO> getPopularCourses();
}
