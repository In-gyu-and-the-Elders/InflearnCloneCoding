package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.admin.AdminLoginDTO;
import inflearn_clone.springboot.service.admin.AdminServiceIf;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrator")
@RequiredArgsConstructor
@Log4j2
public class AdminLoginController {
    private final AdminServiceIf adminService;

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("adminLoginDTO", new AdminLoginDTO());
        return "admin/login";
    }


    /**
     * 관리자 로그인
     *
     */
    @PostMapping("/login")
    public String loginProcess(
            @Valid @ModelAttribute("adminLoginDTO") AdminLoginDTO loginDTO, // @ModelAttribute 추가
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        // 유효성 검증 실패 처리
        if (bindingResult.hasErrors()) {
            model.addAttribute("adminLoginDTO", loginDTO); // 입력값 유지
            return "admin/login"; // 로그인 페이지로 다시 이동
        }

        // 로그인 처리
        if (adminService.loginAdmin(loginDTO)) {
            session.setAttribute("adminId", loginDTO.getAdminId());
            return "redirect:/admin/dashboard"; // 대시보드로 리다이렉트
        }

        // 로그인 실패 처리
        model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        return "admin/login";
    }


    /**
     * 관리자 로그아웃
     *
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/administrator/login";
    }
}
