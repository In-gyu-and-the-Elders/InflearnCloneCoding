package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.service.admin.NoticeServiceIf;
import inflearn_clone.springboot.utils.JSFunc;
import inflearn_clone.springboot.utils.Paging;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static inflearn_clone.springboot.utils.QueryUtil.generateSortQuery;
@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
@Log4j2
public class NoticeController {
    private final NoticeServiceIf noticeService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder) {

        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = noticeService.noticeTotalCntS(searchCategory, searchValue);
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<BbsDTO> notice =  noticeService.listS(pageNo, 10, searchCategory, searchValue, sortQuery);

        log.info("totalCnt:{}", totalCnt);
        log.info("notice:{}", notice);
        for( BbsDTO bbsDTO : notice){
            bbsDTO.setFileName();
            bbsDTO.setExt();
        }
        model.addAttribute("notice", notice);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/notice/list");
        return "notice/list";
    }

    @GetMapping("/view")
    public String view(@RequestParam int idx, HttpServletResponse response, Model model) {
        BbsDTO bbsDTO = noticeService.view(idx);
        if(bbsDTO != null){
            bbsDTO.setFileName();
            bbsDTO.setExt();
            model.addAttribute("bbsDTO",bbsDTO);
            return "notice/view";
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("해당 글은 존재하지 않습니다.", response);
            return null;
        }
    }
}
