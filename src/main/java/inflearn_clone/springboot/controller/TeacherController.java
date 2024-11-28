package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.service.CourseSerivce;
import inflearn_clone.springboot.service.section.SectionServiceIf;
import inflearn_clone.springboot.utils.CommonFileUtil;
import inflearn_clone.springboot.utils.JSFunc;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final CourseSerivce courseSerivce;
    private final SectionServiceIf sectionService;
    @GetMapping("/course/insert")
    public String insert() {
        return "teacher/course/insert";
    }

    @PostMapping("/course/insert")
    public String insert(CourseDTO courseDTO, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        courseDTO.setTeacherId("임시선생님");
        try {
            String filePath = CommonFileUtil.uploadFile(courseDTO.getThumbnailFile());
            courseDTO.setThumbnail(filePath);
        } catch( IOException e){
            log.error(e);
        }
        int result = courseSerivce.insertCourse(courseDTO);
        if(result > 0){
            return "redirect:/teacher/course/insert_s";
        } else {
            JSFunc.alertBack("강좌 등록에 실패했습니다. 다시 시도해주세요",response);
            return null;
        }
    }

    @GetMapping("/course/insert_s")
    public String insert_s(Model model) {
        CourseDTO courseDTO = courseSerivce.viewMyLastCourse("임시선생님");
        model.addAttribute("courseDTO", courseDTO);
        return "teacher/course/insert_s";
    }

    @PostMapping("/course/insert_s")
    @Transactional
    public String insert_s(@RequestParam("sections") String[] sections,
                           @RequestParam("courseIdx") int courseIdx,
                           HttpServletResponse response) {
       try {
           for (String section : sections) {
               SectionDTO sectionDTO = SectionDTO.builder()
                       .courseIdx(courseIdx)
                       .section(section).build();
               sectionService.insertSection(sectionDTO);
           }
           return "redirect:/teacher/course/insert_l";
       } catch (Exception e) {
           log.error(e);
           log.info("catch됨");
           JSFunc.alertBack("섹션등록실패",response);
           return null;
       }
    }

    @GetMapping("/course/insert_l")
    public String insert_l(Model model) {
        int courseIdx = courseSerivce.viewMyLastCourse("임시선생님").getIdx();
        List<SectionDTO> sectionDTOList = sectionService.sectionList(courseIdx);
        model.addAttribute("sections", sectionDTOList);
        return "teacher/course/insert_l";
    }
}
