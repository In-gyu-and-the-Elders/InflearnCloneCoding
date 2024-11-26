package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.service.member.MemberServiceIf;
import inflearn_clone.springboot.utils.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final MemberServiceIf memberServiceIf;
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

    @GetMapping("/member/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder){
        int totalCnt = memberServiceIf.memberTotalCnt(searchCategory, searchValue);
        log.info("Member totalCnt" + totalCnt); // 100
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<MemberDTO> members =  memberServiceIf.selectAllMember(pageNo, 10, searchCategory, searchValue);
        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        return "admin/member/list";
    }
}
