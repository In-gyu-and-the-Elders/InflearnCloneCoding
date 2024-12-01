package inflearn_clone.springboot.service.main;

import java.util.List;

import inflearn_clone.springboot.dto.main.MainDTO;

public interface MainServiceIf {
  List<MainDTO> getRecentCourses();
  List<MainDTO> getCoursesByCategory(String category);
  List<MainDTO> getPopularCourses();
}
