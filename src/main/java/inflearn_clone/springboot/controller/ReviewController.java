package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.service.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/regist")
    public String registerReview(@RequestParam("courseIdx") int courseIdx,
                                 @RequestParam("rating") int rating,
                                 @RequestParam("content") String content,
                                 HttpServletRequest request, HttpServletResponse response
                                 ) {
//        String memberId = "user1"; // 테스트용
        // 로그인 후 삭제
        String memberId = (String) request.getSession().getAttribute("memberId");
        log.info("courseIdx{}",courseIdx );
        ReviewListDTO review = new ReviewListDTO();
        review.setCourseIdx(courseIdx);
        review.setRating(rating);
        review.setContent(content);
        review.setMemberId(memberId);
        log.info("review{}",review);
        reviewService.registerReview(review);
        return "redirect:/course/tab/review/" + review.getCourseIdx();
    }
    // 리뷰 조회 (Ajax 요청)
    @GetMapping("/{idx}")
    @ResponseBody
    public ReviewListDTO viewReview(@PathVariable("idx") int idx) {
        ReviewListDTO review = reviewService.viewReview(idx);

        // 로그 출력 (리뷰 객체의 값 확인)
        log.info("수자ㅓㅇ 겟review{}: " , review);

        return review;
    }

    // 리뷰 수정
    @PostMapping("/modify")
    public String modifyReview(@ModelAttribute ReviewListDTO review) {
        log.info("수정 포스트 시작");
        log.info("수정포스트 review{}",review);
        reviewService.modifyReview(review);
        return "redirect:/course/tab/review/" + review.getCourseIdx();
    }

    // 리뷰 삭제
    @PostMapping("/delete")
    public String deleteReview(@RequestParam int idx, @RequestParam int courseIdx) {
        reviewService.deleteReview(idx);
        return "redirect:/course/tab/review/" + courseIdx;
    }
}
