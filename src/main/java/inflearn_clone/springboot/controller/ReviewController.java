package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.service.review.ReviewService;
import inflearn_clone.springboot.utils.JSFunc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/regist")
    public void registerReview(@ModelAttribute ReviewListDTO review,
                               HttpServletRequest request, HttpServletResponse response) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        review.setMemberId(memberId);

        // 리뷰 작성 여부 확인
        boolean hasWrittenReview = reviewService.writerCheck(memberId, review.getCourseIdx());
        if (hasWrittenReview) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("이미 리뷰를 작성하셨습니다.", response);
            return;
        }

        try {
            reviewService.registerReview(review);
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("리뷰가 등록되었습니다.", "/course/view/" + review.getCourseIdx(), response);
        } catch (RuntimeException e) {
            log.error("리뷰 등록 오류: {}", e.getMessage());
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack(e.getMessage(), response);
        }
    }


    // 리뷰 조회 (Ajax 요청)
    @GetMapping("/{idx}")
    @ResponseBody
    public ReviewListDTO viewReview(@PathVariable("idx") int idx, HttpServletRequest request) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        ReviewListDTO review = reviewService.viewReview(idx);
        if (review != null && review.getMemberId().equals(memberId)) {
            return review;
        }
        return null;
    }

    @PostMapping("/modify")
    @ResponseBody
    public ResponseEntity<Map<String, String>> modifyReview(@ModelAttribute ReviewListDTO review,
                                                            HttpServletRequest request) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        review.setMemberId(memberId);

        Map<String, String> response = new HashMap<>();

        try {
            int result = reviewService.modifyReview(review);
            if (result > 0) {
                response.put("status", "success");
                response.put("message", "리뷰가 수정되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "리뷰를 수정할 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (RuntimeException e) {
            log.error("리뷰 수정 오류: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "리뷰 수정에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteReview(@RequestParam int idx, HttpServletRequest request) {
        String memberId = (String) request.getSession().getAttribute("memberId");

        Map<String, String> response = new HashMap<>();

        try {
            int result = reviewService.deleteReview(idx, memberId);
            if (result > 0) {
                response.put("status", "success");
                response.put("message", "리뷰가 삭제되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "리뷰를 삭제할 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (RuntimeException e) {
            log.error("리뷰 삭제 오류: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "리뷰 삭제에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @ResponseBody
    public int getReviewCount(@RequestParam int courseIdx) {
        return reviewService.reviewCnt(courseIdx);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ReviewListDTO> getReviewList(@RequestParam int courseIdx,
                                             @RequestParam String sortBy,
                                             @RequestParam int page) {
        int limit = 5;
        int offset = page * limit;
        return reviewService.getReviewList(courseIdx, sortBy, offset, limit);
    }

}
