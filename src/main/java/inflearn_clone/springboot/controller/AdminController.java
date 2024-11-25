package inflearn_clone.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    /*
    * 관리자 로그인 페이지 이동
     */
    @GetMapping("/login")
    public String loginPage(){
        return "admin/login";
    }

    /*
    * 관리자 로그인
    */
    @PostMapping("/login")
    public String login(){
        return "admin/index";
    }

    /*
     * 관리자 로그아웃
     */
    @PostMapping("/logout")
    public String logout(){
        return "admin/login";
    }

    /*
     * 관리자 대시보드 이동
     * 강의 수(상태 마다), 회원(상태 마다), 문의 메뉴 수
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        model.addAttribute("");
        return "admin/dashboard";
    }

    @GetMapping("/list")
    public String list(Model model){
        return "admin/list";
    }


}
