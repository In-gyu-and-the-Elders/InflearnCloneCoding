package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.service.course.CourseSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseSerivce courseSerivce;
    @GetMapping("/list")
    public String courseList(Model model) {
        List<CourseDTO> courseList = courseSerivce.courseList();
        model.addAttribute("courseList", courseList);
//        for (CourseDTO course : courseList) {
//            log.info(course.getDescription());
//        }
//        log.info("Course List Size: {}", courseList.size());
//        log.info("courseList{}",courseList);
        return "course/list";
    }

    // 상세보기 페이지
    @GetMapping("/view/{idx}")
    public String courseView(@PathVariable int idx, Model model) {
        CourseDTO course = courseSerivce.courseView(idx);
        model.addAttribute("course", course);
        return "course/view";
    }

    @GetMapping("/tab/{tabName}/{idx}")
    public String getTabContent(@PathVariable String tabName,
                                @PathVariable int idx
                                ,Model model) {
        switch (tabName) {
            case "info":
                CourseDTO course = courseSerivce.courseView(idx);
                model.addAttribute("course", course);
                return "course/tabs/info";
            case "curriculum":
                return "course/tabs/curriculum";
            case "qna":
//                model.addAttribute("qnaList", qnaService.getQnAList());
                return "course/tabs/qna";
            case "review":
//                model.addAttribute("reviewList", reviewService.getReviewList());
                return "course/tabs/review";
            case "notice":
//                model.addAttribute("noticeList", noticeService.getNoticeList());
                return "course/tabs/notice";
            default:
                return "/";
        }
    }
}
