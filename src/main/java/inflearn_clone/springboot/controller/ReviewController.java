package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.service.review.ReviewService;
import inflearn_clone.springboot.utils.JSFunc;
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
        log.info("courseIdx{}", courseIdx);
        ReviewListDTO review = new ReviewListDTO();
        review.setCourseIdx(courseIdx);
        review.setRating(rating);
        review.setContent(content);
        review.setMemberId(memberId);
        log.info("review{}", review);
        reviewService.registerReview(review);
        return "redirect:/course/tab/review/" + review.getCourseIdx();
    }

    // 리뷰 조회 (Ajax 요청)
    @GetMapping("/{idx}")
    @ResponseBody
    public ReviewListDTO viewReview(@PathVariable("idx") int idx) {
        ReviewListDTO review = reviewService.viewReview(idx);
        log.info("수자ㅓㅇ 겟review{}: ", review);
        return review;
    }

    // 리뷰 수정
    @PostMapping("/modify")
    public void modifyReview(@ModelAttribute ReviewListDTO review, HttpServletRequest request,HttpServletResponse response) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        log.info("수정포스트 review{}", review);
        review.setMemberId(memberId);
        int result = reviewService.modifyReview(review);
        // 수정 성공 여부 체크
        if (result > 0) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("리뷰 수정이 완료되었습니다.", "/course/tab/review/" + review.getCourseIdx(), response);
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("리뷰 수정에 실패하였습니다. 다시 시도해주세요.", response);
        }
    }

// 리뷰 삭제
@PostMapping("/delete")
public String deleteReview(@RequestParam int idx, @RequestParam int courseIdx) {
    reviewService.deleteReview(idx);
    return "redirect:/course/tab/review/" + courseIdx;
}
}
