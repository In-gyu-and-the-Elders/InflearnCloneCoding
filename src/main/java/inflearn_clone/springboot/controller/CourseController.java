package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseTotalDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.dto.section.SectionWithLessonListDTO;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.service.cart.CartService;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.service.lesson.LessonServiceIf;
import inflearn_clone.springboot.service.like.LikeService;
import inflearn_clone.springboot.service.order.OrderService;
import inflearn_clone.springboot.service.review.ReviewService;
import inflearn_clone.springboot.service.section.SectionServiceIf;
import inflearn_clone.springboot.utils.Paging;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final SectionServiceIf sectionService;
    private final LessonServiceIf lessonService;
    private final ModelMapper modelMapper;

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
        String memberId = (String) request.getSession().getAttribute("memberId");
//        String memberId = "user1";
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
                                HttpServletRequest request,
                                Model model) {
        switch (tabName) {
            case "info":
                CourseDTO course = courseSerivce.courseView(idx);
                model.addAttribute("course", course);
                return "course/tabs/info";
            case "curriculum":


                //여기서 해야 하는 것 --> 이미 이 강좌 뷰에 들어왔으니까, 섹션 리스트가 필요한데, 이 섹션에는 강의 리스트가 포함이 되어 있어야 함!
                List<SectionDTO> sectionDTOList = sectionService.sectionList(idx); // 강좌 인덱스로 섹션의 리스트를 가져옴.
                List<SectionWithLessonListDTO> sectionWithLessonListDTOList = new ArrayList<>(); // 강의 리스트가 포함된 섹션 리스트를 생성
                for(SectionDTO sectionDTO : sectionDTOList){ // 돌면서 이제 sectionDTO에 리스트 정보를 넣어주어야 함.
                    int sectionIdx1 = sectionDTO.getIdx();
                    log.info("sectionIdx1:{}",sectionIdx1);
                    List<LessonDTO> lessons = lessonService.getLessons(sectionIdx1);
                    log.info("lessons:{}",lessons);
                    SectionWithLessonListDTO sectionWithLessonListDTO = modelMapper.map(sectionDTO, SectionWithLessonListDTO.class);
                    log.info("sectionWithLessonListDTO:{}",sectionWithLessonListDTO);
                    int lessonCount = lessons.size();
                    sectionWithLessonListDTO.setLessonCount(lessonCount);
                    sectionWithLessonListDTO.setLessons(lessons);
                    sectionWithLessonListDTOList.add(sectionWithLessonListDTO);
                } //여기까지 했으면 이제 섹션과 해당 강의가 같이 말아짐
                model.addAttribute("sectionList", sectionWithLessonListDTOList);
                return "course/tabs/curriculum";
//            case "qna":
////                model.addAttribute("qnaList", qnaService.getQnAList());
//                return "course/tabs/qna";
            case "review":
//                log.info("실제 idx"+idx);
//                log.info("실제 sortBy"+sortBy);
//                log.info("실제 page"+page);
                String memberId = (String) request.getSession().getAttribute("memberId");
                List<ReviewListDTO> reviews = reviewService.getReviewList(idx, sortBy, page);
                model.addAttribute("memberId", memberId);
                model.addAttribute("reviews", reviews);
                model.addAttribute("courseIdx", idx);
                model.addAttribute("currentPage", page);

//                log.info("reviews{}",reviews);
                return "course/tabs/review";
//            case "notice":
////                model.addAttribute("noticeList", noticeService.getNoticeList());
//                return "course/tabs/notice";
            default:
                return "/";
        }
    }

}
