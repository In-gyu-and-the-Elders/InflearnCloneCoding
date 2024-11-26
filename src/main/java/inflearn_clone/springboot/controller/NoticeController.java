package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.service.admin.NoticeServiceIf;
import inflearn_clone.springboot.utils.CommonFileUtil;
import inflearn_clone.springboot.utils.JSFunc;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
@Log4j2
public class NoticeController {
    private final NoticeServiceIf noticeService;



    @GetMapping("/insert")
    public String insert() {
        return "notice/insert";
    }
    @PostMapping("/insert")
    public String insert(BbsDTO bbsDTO,
                         Model model, HttpServletResponse response) { //validation 추가해야 함.
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
            return "redirect:/notice/list";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("등록에 실패했습니다", response);
            return null;
        }
    }

    @GetMapping("/view")
    public String view(@RequestParam int idx, HttpServletResponse response, Model model) {
        BbsDTO bbsDTO = noticeService.view(idx);
        if(bbsDTO != null){
            model.addAttribute("bbsDTO",bbsDTO);
            return "notice/view";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("해당 글은 존재하지 않습니다.", response);
            return null;
        }
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("noticeList", noticeService.list());
        return "notice/list";
    }

    @GetMapping("/modify")
    public String modify(@RequestParam int idx, HttpServletResponse response, Model model) {
        BbsDTO bbsDTO = noticeService.view(idx);
        if(bbsDTO != null){
            model.addAttribute("bbsDTO",bbsDTO);
            return "notice/modify";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("해당 글은 존재하지 않습니다.", response);
            return null;
        }
    }

    @PostMapping("/modify")
    public String modify(@RequestParam int idx, BbsDTO bbsDTO, Model model, HttpServletResponse response) {
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
            return "redirect:/notice/view?idx=" + idx;
        } else {
            JSFunc.alertBack("수정에 실패했습니다. 다시 시도해주세요.",response);
            return null;
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int idx, HttpServletResponse response) {
        int result = noticeService.delete(idx);
        if(result > 0){
            return "redirect:/notice/list";
        } else {
            JSFunc.alertBack("공지사항 삭제 실패",response);
            return null;
        }
    }


}
