package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.service.CourseSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseSerivce courseSerivce;
    @GetMapping("/list")
    public String courseList() {
//        List<CourseDTO> courseList = courseSerivce.courseList();
//        model.addAttribute("courseList", courseList);
        return "course/list";
    }

    // 상세보기 페이지
    @GetMapping("/view")
    public String courseView() {
//        CourseDTO course = courseSerivce.courseView(idx);
//        model.addAttribute("course", course);
        return "course/view";
    }
}
