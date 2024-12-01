package inflearn_clone.springboot.service.main;

import inflearn_clone.springboot.dto.main.MainDTO;
import inflearn_clone.springboot.mappers.MainMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainServiceIf {
    
    private final MainMapper mainMapper;
    
    @Override
    public List<MainDTO> getRecentCourses() {
        return mainMapper.getRecentCourses();
    }
    
    @Override
    public List<MainDTO> getCoursesByCategory(String category) {
        return mainMapper.getCoursesByCategory(category);
    }
    
    @Override
    public List<MainDTO> getPopularCourses() {
        return mainMapper.getPopularCourses();
    }
}
