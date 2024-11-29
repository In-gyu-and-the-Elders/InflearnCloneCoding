package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/regist")
    @ResponseBody
    public Map<String, Object> toggleLike(@RequestParam("courseIdx") int courseIdx) {
//        log.info("courseIdx: " + courseIdx);
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
        String memberId = "user1";
        // 좋아요 토글
        likeService.toggleLike(courseIdx, memberId);

        // 좋아요 상태 확인
        boolean isLiked = likeService.likeCheck(courseIdx, memberId);

        // 좋아요 수 가져오기
        int likeCount = likeService.getLikeCount(courseIdx);

        // 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", likeCount);
        response.put("isLiked", isLiked);
        return response;
    }
}
