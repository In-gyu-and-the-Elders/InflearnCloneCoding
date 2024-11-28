package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.config.JwtTokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/")
    public String main(Model model) {
        return "main";
    }

    @GetMapping("/sign/signUp")
    public String signUp() {
        return "sign/signUp";
    }

    @GetMapping("/sign/signIn")
    public String signIn() {
        return "sign/signIn";
    }

    @GetMapping("/member/myPage")
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
