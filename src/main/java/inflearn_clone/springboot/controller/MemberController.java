package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.config.JwtTokenProvider;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;
import inflearn_clone.springboot.service.myPage.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final JwtTokenProvider jwtTokenProvider;
  private final MyPageService myPageService;

  @GetMapping("/myPage")
  public String myPage(HttpSession session, Model model) {
    String token = (String) session.getAttribute("token");
    String memberType = (String) session.getAttribute("memberType");
    String memberId = (String) session.getAttribute("memberId");

    log.info("Token: " + token);
    log.info("MemberType: " + memberType);
    log.info("MemberId: " + memberId);

    if (token == null) {
      log.warn("No token found in session");
      model.addAttribute("error", "로그인이 필요합니다.");
      return "common/errorPage";
    }

    try {
      if (!jwtTokenProvider.validateToken(token)) {
        log.warn("Invalid token");
        model.addAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
        return "common/errorPage";
      }

      if ("S".equals(memberType)) {
        List<MyPageDTO> myCourses = myPageService.getMyCourses(memberId);
        model.addAttribute("myCourses", myCourses);
        log.info("My courses: " + myCourses);
        log.info("MemberId: " + memberId);
        model.addAttribute("memberId", memberId);
        return "member/studentMyPage";
      } else if ("T".equals(memberType)) {
        model.addAttribute("memberId", memberId);
        return "teacher/teacherMyPage";
      } else {
        log.warn("Invalid member type: " + memberType);
        model.addAttribute("error", "잘못된 회원 유형입니다.");
        return "common/errorPage";
      }

    } catch (Exception e) {
      log.error("Error during token validation", e);
      model.addAttribute("error", "인증 처리 중 오류가 발생했습니다.");
      return "common/errorPage";
    }
  }
}
