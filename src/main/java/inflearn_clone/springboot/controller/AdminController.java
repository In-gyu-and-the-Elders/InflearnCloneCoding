package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.service.admin.NoticeServiceIf;
import inflearn_clone.springboot.service.member.MemberServiceIf;
import inflearn_clone.springboot.utils.CommonFileUtil;
import inflearn_clone.springboot.utils.JSFunc;
import inflearn_clone.springboot.utils.Paging;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

import static inflearn_clone.springboot.utils.QueryUtil.generateSortQuery;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final MemberServiceIf memberService;
    private final NoticeServiceIf noticeService;
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








    //여기부터 아래는 인규가 작업한 부분입니다.
    // [관리자 공지사항]

    // 11261051 --> validation 아직 안 됨
    @GetMapping("notice/insert")
    public String insert() {
        return "admin/notice/insert";
    }

    @PostMapping("notice/insert")
    public String insert(BbsDTO bbsDTO, HttpServletResponse response) {
        bbsDTO.setWriterId("testUser1"); //아직 세션 없음

        bbsDTO.setCategory("N"); // [공지사항]
        //1. 파일 업로드
        String fileName = null;
        try {
            fileName = CommonFileUtil.uploadFile(bbsDTO.getFile());
        } catch (IOException e) {
            log.info("[NoticeController] >> [insert] >> fileUpload Failed : {}",e.getMessage());
            // 파일 업로드 실패
            JSFunc.alertBack("파일 업로드 실패. 다시 시도해주세요", response);
        }
        // 파일 업로드 성공
        if(fileName!=null && !fileName.isEmpty()){
            bbsDTO.setFilePath(fileName);
        }
        int result = noticeService.insert(bbsDTO);

        if(result > 0){
            return "redirect:/admin/notice/list";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("등록에 실패했습니다", response);
            return null;
        }
    }

    @GetMapping("notice/view")
    public String view(@RequestParam int idx, HttpServletResponse response, Model model) {
        BbsDTO bbsDTO = noticeService.view(idx);
        if(bbsDTO != null){
            bbsDTO.setFileName();
            bbsDTO.setExt();
            model.addAttribute("bbsDTO",bbsDTO);
            return "admin/notice/view";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("해당 글은 존재하지 않습니다.", response);
            return null;
        }
    }

    @GetMapping("notice/list")
    public String list(Model model) {
        List<BbsDTO> list = noticeService.list();
        for( BbsDTO bbsDTO : list){
            bbsDTO.setFileName();
            bbsDTO.setExt();
        }
        model.addAttribute("notice", list);
        return "admin/notice/list";
    }

    @GetMapping("notice/modify")
    public String modify(@RequestParam int idx, HttpServletResponse response, Model model) {
        BbsDTO bbsDTO = noticeService.view(idx);
        if(bbsDTO != null){
            bbsDTO.setFileName();
            bbsDTO.setExt();
            model.addAttribute("bbsDTO",bbsDTO);
            return "admin/notice/modify";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("해당 글은 존재하지 않습니다.", response);
            return null;
        }
    }

    @PostMapping("notice/modify")
    public String modify(@RequestParam int idx, BbsDTO bbsDTO, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        String originalFilePath = noticeService.view(idx).getFilePath(); //원본 파일명
        String newFilePath = null;

        try {
            newFilePath = CommonFileUtil.uploadFile(bbsDTO.getFile()); // 새로운 파일 업로드
        } catch (IOException e){
            log.info(e.getMessage());
        }
        bbsDTO.setFilePath(newFilePath);

        if (newFilePath != null && !newFilePath.isEmpty()) {
            boolean fileDeleted = CommonFileUtil.deleteFile(originalFilePath);
            if (!fileDeleted) {
                log.info("기존 파일 삭제 실패");
            }
            bbsDTO.setFilePath(newFilePath);
        }

        int result = noticeService.update(bbsDTO);
        if (result > 0) {
            return "redirect:/admin/notice/view?idx=" + idx;
        } else {
            JSFunc.alertBack("수정에 실패했습니다. 다시 시도해주세요.",response);
            return null;
        }
    }

    @GetMapping("notice/delete") // 현재 페이지를 가져오든가, ajax로 처리를 해보든가
    public String delete(@RequestParam int idx, HttpServletResponse response) {
        int result = noticeService.delete(idx);
        if(result > 0){
            return "redirect:/admin/notice/list";
        } else {
            JSFunc.alertBack("공지사항 삭제 실패",response);
            return null;
        }
    }


}
