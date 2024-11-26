package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.service.member.MemberServiceIf;
import inflearn_clone.springboot.utils.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static inflearn_clone.springboot.utils.QueryUtil.generateSortQuery;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final MemberServiceIf memberService;
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

    /*
    * 회원 목록
    */
    @GetMapping("/member/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder){
        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = memberService.memberTotalCnt(searchCategory, searchValue);
        log.info("Member totalCnt" + totalCnt); // 100
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<MemberDTO> members =  memberService.selectAllMember(pageNo, 10, searchCategory, searchValue, sortQuery);
        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("url", "/admin/member/list");
        return "admin/member/list";
    }

    /*
    * 회원 조회
    */
    @GetMapping("/member/view")
    public String memberView(Model model,
                             @RequestParam String memberId){
        MemberDTO memberInfo = memberService.selectMemberInfo(memberId);
        model.addAttribute("info", memberInfo);
        if(memberInfo != null){
            return "admin/member/view";
        }else{
            log.info("회원 정보 없음");
        }
        return null;
    }

    /*
     * 회원 수정 폼 전달
     */
    @GetMapping("/member/modify")
    public String modifyGet(Model model,
                            @RequestParam String memberId){
        MemberDTO memberInfo = memberService.selectMemberInfo(memberId);
        model.addAttribute("info", memberInfo);
        if(memberInfo != null){
            return "admin/member/modify";
        }else{
            log.info("회원 정보 없음");
        }
        return null;
    }

    /*
     * 회원 수정
     * 회원 수정 후 회원 상세 페이지로 이동
     */
    @PostMapping("/member/modify")
    public String modifyPost(MemberDTO dto){
        System.out.println(dto.getName());
        boolean result = memberService.modifyMemberInfo(dto);
        System.out.println("result" + result);
        if(result){
            return "admin/member/list"; //나중에 view로 바꿀 것
        }
        return null;
    }
}
