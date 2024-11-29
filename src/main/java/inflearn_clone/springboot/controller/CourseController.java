package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseTotalDTO;
import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.service.cart.CartService;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.service.like.LikeService;
import inflearn_clone.springboot.service.order.OrderService;
import inflearn_clone.springboot.service.review.ReviewService;
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

import java.util.ArrayList;
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
    private final LikeService likeService;
    private final ReviewService reviewService;

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
        List<CourseTotalDTO> courseList  = courseSerivce.getCourses(pageNo, 8, searchCategory, searchValue, sortQuery);
//        log.info("course{}",course);
//        log.info("searchCategory"+searchCategory);
//        log.info("searchValue"+searchValue);

        for (CourseTotalDTO course : courseList) {
            int studentCount = orderService.studentCnt(course.getIdx());
            course.setStudentCount(studentCount); // 수강생 수를 DTO에 추가
        }
        model.addAttribute("course", courseList);
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
        // 상세보기
        CourseDTO course = courseSerivce.courseView(idx);
        // 장바구니 중복확인
        boolean isCartPossible = cartService.cartCnt(course.getIdx(), memberId);
        // 결제내역 확인
        boolean isOrderPossible = orderService.orderCnt(course.getIdx(), memberId);
        // 강의별 수강생 수 확인
        int studentCnt = orderService.studentCnt(idx);
        // 좋아요수
        int likeCount = likeService.getLikeCount(idx);
        // 좋아요회원확인
        boolean isLiked = likeService.likeCheck(idx, memberId);
        model.addAttribute("isCartPossible", isCartPossible);
        model.addAttribute("isOrderPossible", isOrderPossible);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("studentCnt", studentCnt);
        model.addAttribute("memberId", memberId);
        model.addAttribute("course", course);
        return "course/view";
    }

    @GetMapping("/tab/{tabName}/{idx}")
    public String getTabContent(@PathVariable String tabName,
                                @PathVariable int idx, //강좌idx
                                @RequestParam(defaultValue = "latest") String sortBy,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        switch (tabName) {
            case "info":
                CourseDTO course = courseSerivce.courseView(idx);
                model.addAttribute("course", course);
                return "course/tabs/info";
            case "curriculum":
                return "course/tabs/curriculum";
//            case "qna":
////                model.addAttribute("qnaList", qnaService.getQnAList());
//                return "course/tabs/qna";
            case "review":
                log.info("실제 idx"+idx);
                log.info("실제 sortBy"+sortBy);
                log.info("실제 page"+page);
                List<ReviewListDTO> reviews = reviewService.getReviewList(idx, sortBy, page);

                model.addAttribute("reviews", reviews);
                model.addAttribute("courseIdx", idx);
                model.addAttribute("currentPage", page);

                log.info("reviews{}",reviews);
                return "course/tabs/review";
//            case "notice":
////                model.addAttribute("noticeList", noticeService.getNoticeList());
//                return "course/tabs/notice";
            default:
                return "/";
        }
    }
}
