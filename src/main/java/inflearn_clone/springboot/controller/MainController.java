package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.config.JwtTokenProvider;
import inflearn_clone.springboot.dto.main.MainDTO;
import inflearn_clone.springboot.service.main.MainServiceImpl;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final MainServiceImpl mainService;

    @GetMapping("/")
    public String main(Model model) {
        List<MainDTO> popularCourses = mainService.getPopularCourses();
        List<MainDTO> recentCourses = mainService.getRecentCourses();
        
        model.addAttribute("popularCourses", popularCourses);
        model.addAttribute("recentCourses", recentCourses);
        
        Map<String, List<MainDTO>> categoryCourses = new HashMap<>();
        String[] categories = {"D", "A", "H", "C", "M"};
        for (String category : categories) {
            List<MainDTO> courses = mainService.getCoursesByCategory(category);
            categoryCourses.put(category, courses);
        }
        model.addAttribute("categoryCourses", categoryCourses);
        
        return "main";
    }
}
