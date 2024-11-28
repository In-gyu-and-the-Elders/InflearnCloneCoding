package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.service.cart.CartService;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.service.order.OrderService;
import inflearn_clone.springboot.utils.Paging;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static inflearn_clone.springboot.utils.QueryUtil.generateSortQuery;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseSerivce courseSerivce;
    private final CartService cartService;
    private final OrderService orderService;
//    @GetMapping("/list")
//    public String courseList(Model model) {
//        List<CourseDTO> courseList = courseSerivce.courseList();
//        model.addAttribute("courseList", courseList);
////        for (CourseDTO course : courseList) {
////            log.info(course.getDescription());
////        }
////        log.info("Course List Size: {}", courseList.size());
////        log.info("courseList{}",courseList);
//        return "course/list";
//    }
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder) {
        String sortQuery = generateSortQuery(sortType, sortOrder);

        // 총 강의 수 가져오기
        int totalCnt = courseSerivce.getTotalCourses(searchCategory, searchValue);
//        log.info("강좌수:"+totalCnt);

        // 페이징 처리
        Paging paging = new Paging(pageNo, 8, 5, totalCnt, sortType, sortOrder);

        // 강의 리스트 가져오기
        List<CourseDTO> course = courseSerivce.getCourses(pageNo, 8, searchCategory, searchValue, sortQuery);
//        log.info("course{}",course);
//        log.info("searchCategory"+searchCategory);
//        log.info("searchValue"+searchValue);
        // Model에 값 전달
        model.addAttribute("course", course);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("url", "/course/list");

        return "course/list";
    }
    // 상세보기 페이지
    @GetMapping("/view/{idx}")
    public String courseView(@PathVariable int idx, Model model,
                             HttpServletRequest request, HttpServletResponse response) {
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
        String memberId = "user1";
        CourseDTO course = courseSerivce.courseView(idx);
        boolean isCartPossible = cartService.cartCnt(course.getIdx(), memberId);
        boolean isOrderPossible = orderService.orderCnt(course.getIdx(), memberId);
        model.addAttribute("isCartPossible", isCartPossible);
        model.addAttribute("isOrderPossible", isOrderPossible);
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
