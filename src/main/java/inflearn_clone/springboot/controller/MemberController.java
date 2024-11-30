package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.config.JwtTokenProvider;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;
import inflearn_clone.springboot.service.myPage.MyPageService;
import inflearn_clone.springboot.service.sign.SignServiceImpl;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final JwtTokenProvider jwtTokenProvider;
  private final MyPageService myPageService;
  private final SignServiceImpl signService;

  @GetMapping("/myPage")
  public String myPage(HttpSession session, Model model, @RequestParam(defaultValue = "latest") String sortType) {
    String token = (String) session.getAttribute("token");
    String memberType = (String) session.getAttribute("memberType");
    String memberId = (String) session.getAttribute("memberId");

    log.info("Token: " + token);
    log.info("MemberType: " + memberType);
    log.info("MemberId: " + memberId);
    log.info("Sort Type: " + sortType);

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
        List<MyPageDTO> myCourses = myPageService.getMyCourses(memberId, sortType);
        model.addAttribute("myCourses", myCourses);
        model.addAttribute("currentSort", sortType);
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

  @GetMapping("/likedCourses")
  public String likedCourses(HttpSession session, Model model, @RequestParam(defaultValue = "latest") String sortType) {
    String token = (String) session.getAttribute("token");
    String memberId = (String) session.getAttribute("memberId");

    if (token == null) {
      log.warn("No token found in session");
      model.addAttribute("error", "로그인이 필요합니다.");
      return "common/errorPage";
    }

    List<MyPageDTO> likedCourses = myPageService.getLikedCourses(memberId, sortType);
    model.addAttribute("likedCourses", likedCourses);
    model.addAttribute("currentSort", sortType);
    model.addAttribute("memberId", memberId);
    return "member/likedCourses";
  }
  
   @GetMapping("/accountInfo")
    public String accountInfo(Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId != null) {
            MemberDTO memberInfo = myPageService.accountInfo(memberId);
            model.addAttribute("memberInfo", memberInfo);
            return "member/accountInfo";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateMemberInfo(@RequestBody MemberDTO memberDTO) {
        log.info("updateMemberInfo: {}", memberDTO);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            myPageService.updateMemberInfo(memberDTO);
            response.put("success", true);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            log.error("회원정보 수정 실패: ", e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @GetMapping("/reviews")
    public String getReviews(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        String memberId = (String) session.getAttribute("memberId");

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

            List<MyPageDTO> reviews = myPageService.getReview(memberId);
            model.addAttribute("reviews", reviews);
            model.addAttribute("memberId", memberId);
            return "member/myReview";

        } catch (Exception e) {
            log.error("Error during getting reviews", e);
            model.addAttribute("error", "리뷰 조회 중 오류가 발생했습니다.");
            return "common/errorPage";
        }
    }

    @PostMapping("/review/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateReview(
            @RequestBody Map<String, Object> params,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        
        if (memberId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            int reviewIdx = Integer.parseInt(params.get("reviewIdx").toString());
            String content = (String) params.get("content");
            int rating = Integer.parseInt(params.get("rating").toString());

            boolean result = myPageService.updateReview(reviewIdx, memberId, content, rating);
            
            response.put("success", result);
            if (!result) {
                response.put("message", "리뷰 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("리뷰 수정 중 류 발생", e);
            response.put("success", false);
            response.put("message", "리뷰 수정 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/review/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteReview(
            @RequestBody Map<String, Object> params,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        
        if (memberId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            int reviewIdx = Integer.parseInt(params.get("reviewIdx").toString());
            boolean result = myPageService.deleteReview(reviewIdx, memberId);
            
            response.put("success", result);
            if (!result) {
                response.put("message", "리뷰 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "리뷰 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePassword(
            @RequestBody Map<String, String> params,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        
        if (memberId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            String currentPassword = params.get("currentPassword");
            String newPassword = params.get("newPassword");
            
            // 다 안쳤을 때
            if (currentPassword == null || newPassword == null || 
                currentPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "현재 비밀번호와 새 비밀번호를 모두 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 같은거 쳤을 때
            if (currentPassword.equals(newPassword)) {
                response.put("success", false);
                response.put("message", "현재 비밀번호와 다른 비밀번호를 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            if (newPassword.length() < 8) {
                response.put("success", false);
                response.put("message", "새 비밀번호는 8자 이상이어야 합니다.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean result = signService.updatePassword(memberId, currentPassword, newPassword);
            
            if (result) {
                response.put("success", true);
                response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "현재 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/leave")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> leaveMember(
            @RequestBody Map<String, String> params,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        
        if (memberId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            String leaveReason = params.get("leaveReason");
            
            myPageService.updateMemberLeave(memberId, leaveReason);
            
            session.invalidate();
            
            response.put("success", true);
            response.put("message", "회원 탈퇴가 완료되었습니다.");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("회원 탈퇴 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "회원 탈퇴 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    
}
