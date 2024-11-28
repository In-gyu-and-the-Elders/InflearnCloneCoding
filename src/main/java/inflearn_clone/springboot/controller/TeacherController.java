package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.mappers.SectionMapper;
import inflearn_clone.springboot.service.CourseSerivce;
import inflearn_clone.springboot.service.lesson.LessonServiceIf;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final CourseSerivce courseSerivce;
    private final SectionServiceIf sectionService;
    private final SectionMapper sectionMapper;
    private final LessonServiceIf lessonService;

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
        model.addAttribute("section", sectionDTOList.get(0));
        model.addAttribute("hasNextNext",true);
        return "teacher/course/insert_l";
    }

    @PostMapping("/course/insert_l")
    public String insert_1(@RequestParam("title") String[] titles,
                           @RequestParam("videoFile") MultipartFile[] videoFiles,
                           @RequestParam("sectionIdx") int sectionIdx, Model model,HttpServletResponse response) {


        log.info("title: {}:", Arrays.toString(titles));
        log.info("videoFiles: {}:", Arrays.toString(videoFiles));
        String[] filePaths = new String[videoFiles.length];
        for(int i=0;i<videoFiles.length;i++){
            try {
                filePaths[i] = CommonFileUtil.uploadFile(videoFiles[i]);
            } catch (Exception e) {
                JSFunc.alertBack("영상 업로드 중 오류 발생: "+videoFiles[i],response);
                log.error(e);
                break;
            }
        }
        for(int i = 0;i<videoFiles.length;i++){
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setTitle(titles[i]);
            lessonDTO.setVideo(filePaths[i]);
            lessonDTO.setSectionIdx(sectionIdx);
            int result = lessonService.insertLesson(lessonDTO);
            if(result <= 0){
                JSFunc.alertBack("해당 강의 등록 중 오류 발생 : " + titles[i],response);
                break;
            }
        }

        log.info("sectionIdx: {}",sectionIdx);
        int courseIdx = sectionMapper.getCourseIdBySectionId(sectionIdx); //섹션으로 강좌
        log.info("courseIdx:{}",courseIdx);
        List<Integer> sectionIdList = sectionMapper.sectionIdList(courseIdx); //강좌에서 다시 섹션 리스트 조회
        log.info("sectionIdList:{}",sectionIdList);
        int currentIndex = sectionIdList.indexOf(sectionIdx); //현재 인덱스를 조회할 수 있음.
        log.info("currentIndex:{}",currentIndex);
        boolean hasNext = sectionIdList.contains(sectionIdList.get(currentIndex+1)); // 다음 인덱스가 존재하는지 여부를 체크
        boolean hasNextNext = false;
        try {
            hasNextNext = sectionIdList.contains(sectionIdList.get(currentIndex + 2));
        } catch (Exception e) {
            log.error(e);

        }
        // 다다음 인덱스가 존재하는지 여부를 체크
        log.info("hasNext:{}",hasNext);
        int nextSectionIdx = 0;
        if(hasNext){
           nextSectionIdx = sectionIdList.get(currentIndex+1); // 다음 인덱스로 다음 섹션 번호를 가져옴
        }
        log.info("nextSectionIdx:{}",nextSectionIdx);
        List<SectionDTO> sectionDTOList = sectionService.sectionList(courseIdx);
        model.addAttribute("section", sectionDTOList.get(currentIndex+1)); //다음 섹션을 넘김
        model.addAttribute("hasNextNext", hasNextNext);
        return "teacher/course/insert_l";
    }
}
