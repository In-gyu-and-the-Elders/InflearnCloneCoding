package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.service.course.CourseSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/tab/{tabName}")
    public String getTabContent(@PathVariable String tabName, Model model) {
        switch (tabName) {
            case "info":
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
