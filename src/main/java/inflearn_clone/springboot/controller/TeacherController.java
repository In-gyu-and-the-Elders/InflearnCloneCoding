package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.course.CourseMDTO;
import inflearn_clone.springboot.dto.course.TeacherCourseDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.dto.section.SectionWithLessonListDTO;
import inflearn_clone.springboot.mappers.*;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.service.lesson.LessonServiceIf;
import inflearn_clone.springboot.service.member.MemberServiceIf;
import inflearn_clone.springboot.service.review.ReviewService;
import inflearn_clone.springboot.service.myPage.MyPageService;
import inflearn_clone.springboot.service.section.SectionServiceIf;
import inflearn_clone.springboot.service.teacher.TeacherServiceIf;
import inflearn_clone.springboot.utils.CommonFileUtil;
import inflearn_clone.springboot.utils.JSFunc;
import inflearn_clone.springboot.utils.Paging;
import jakarta.servlet.http.HttpServlet;
import inflearn_clone.springboot.utils.Paging;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static inflearn_clone.springboot.utils.QueryUtil.generateSortQuery;

@Controller
@Log4j2
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final CourseSerivce courseSerivce;
    private final SectionServiceIf sectionService;
    private final SectionMapper sectionMapper;
    private final LessonServiceIf lessonService;
    private final TeacherServiceIf teacherService;
    private final MemberServiceIf memberService;
    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;
    private final ModelMapper modelMapper;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final MyPageService myPageService;
    private final LessonMapper lessonMapper;

    @GetMapping("/course/insert")
    public String insert(@ModelAttribute CourseDTO courseDTO,Model model) {
        model.addAttribute("courseDTO", new CourseDTO());
        return "teacher/course/insert";
    }

    @PostMapping("/course/insert")
    public String insert(@Valid CourseDTO courseDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpServletResponse response, HttpSession session,Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            model.addAttribute("courseDTO", courseDTO);
            return "teacher/course/insert";
        }
        String teacherId = (String) session.getAttribute("memberId");
        courseDTO.setTeacherId(teacherId);
        //파일은 무조건 들어와야 하긴 함. null 신경쓰지 말기
        response.setCharacterEncoding("UTF-8");
        String fileName = courseDTO.getThumbnailFile().getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();  // 확장자 소문자로 변환
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};  // 점 제외한 확장자만 나열
        boolean extensionTest = Arrays.asList(allowedExtensions).contains(fileExtension);

        long fileSize = courseDTO.getThumbnailFile().getSize();
        if(fileSize > 1048576) {
            JSFunc.alertBack("파일 크기는 1MB 이하만 업로드 가능합니다",response);
            return null;
        }
        if(!extensionTest) {
            JSFunc.alertBack("이미지 파일만 업로드 가능합니다 (jpg, jpeg, png)",response);
            return null;
        }
        try {
            String filePath = CommonFileUtil.uploadFile(courseDTO.getThumbnailFile());
            courseDTO.setThumbnail(filePath);
        } catch( IOException e){
            log.error(e);
        }
        int result = courseSerivce.insertCourse(courseDTO);
        if(result > 0){
            session.setAttribute("flag", true);
            return "redirect:/teacher/course/insert_s";
        } else {
            JSFunc.alertBack("강좌 등록에 실패했습니다. 다시 시도해주세요",response);
            return null;
        }
    }

    @GetMapping("/course/insert_s")
    public String insert_s(Model model, HttpSession session, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Boolean flag = (Boolean) session.getAttribute("flag");
        if (flag == null || !flag) {
            // 이전 페이지 정보가 없거나, 올바르지 않으면 리다이렉트
            JSFunc.alertBack("허용되지 않은 접근입니다.",response);
            return null;
        } else {
            session.removeAttribute("flag");
        }
        String teacherId = (String) session.getAttribute("memberId");
        CourseDTO courseDTO = courseSerivce.viewMyLastCourse(teacherId);
        model.addAttribute("courseDTO", courseDTO);
        return "teacher/course/insert_s";
    }
    @GetMapping("/course/insert_ss")
    public String insert_ss(Model model, HttpSession session, @RequestParam int courseIdx,HttpServletResponse response) {
        String teacherId = (String) session.getAttribute("memberId");
        CourseDTO courseDTO = courseSerivce.courseView(courseIdx);
        response.setCharacterEncoding("UTF-8");
        if(!Objects.equals(courseDTO.getTeacherId(), teacherId)){
            JSFunc.alertBack("자신의 강좌만 등록 가능합니다.",response);
            return null;
        }

        model.addAttribute("courseDTO", courseDTO);
        return "teacher/course/insert_s";
    }

    @PostMapping("/course/insert_s")
    @Transactional
    public String insert_s(@RequestParam("sections") String[] sections, // @RequestParam 사용
                           @RequestParam("courseIdx") int courseIdx,
                           HttpServletResponse response, HttpSession session, Model model
                           ) {
        response.setCharacterEncoding("UTF-8");
        if(sections.length <= 0 ){
            session.setAttribute("flag", true);
            JSFunc.alertBack("섹션은 비워둘 수 없습니다.",response);
            return null;
        }
        if (Arrays.stream(sections).anyMatch(String::isEmpty)) {
            session.setAttribute("flag", true);
            JSFunc.alertBack("섹션은 비워둘 수 없습니다.", response);
            return null;
        }



        try {
           for (String section : sections) {
               SectionDTO sectionDTO = SectionDTO.builder()
                       .courseIdx(courseIdx)
                       .section(section).build();
               sectionService.insertSection(sectionDTO);
           }
           session.setAttribute("flagS", true);
           return "redirect:/teacher/course/insert_l";
       } catch (Exception e) {
           log.error(e);
           log.info("catch됨");
           JSFunc.alertBack("섹션등록실패",response);
           return null;
       }
    }

    @GetMapping("/course/insert_l") //치고 들어왔을 때 튕겨야 함
    public String insert_l(Model model, HttpSession session, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Boolean flag = (Boolean) session.getAttribute("flagS");
        if (flag == null || !flag) {
            // 이전 페이지 정보가 없거나, 올바르지 않으면 리다이렉트
            log.info("잘못 들어옴");
            JSFunc.alertBack("허용되지 않은 접근입니다.",response);
            return null;
        } else {
            session.removeAttribute("flagS");
        }
        String teacherId = (String) session.getAttribute("memberId");
        int courseIdx = courseSerivce.viewMyLastCourse(teacherId).getIdx();
        List<SectionDTO> sectionDTOList = sectionService.sectionList(courseIdx);
        model.addAttribute("sectionSize", sectionDTOList.size());
        model.addAttribute("section", sectionDTOList.get(0));
        model.addAttribute("hasNextNext",true);
        return "teacher/course/insert_l";
    }

    @PostMapping("/course/insert_l") //여긴 validation따로 해줘야 함.
    public String insert_1(@RequestParam("title") String[] titles,
                           @RequestParam("videoFile") MultipartFile[] videoFiles,
                           @RequestParam("sectionIdx") int sectionIdx, Model model,HttpServletResponse response
    ,HttpSession session) {
        response.setCharacterEncoding("UTF-8");
        if(titles.length <= 0 ){
            session.setAttribute("flagS", true);
            JSFunc.alertBack("제목은 비워둘 수 없습니다.",response);
            return null;
        }
        if (Arrays.stream(titles).anyMatch(String::isEmpty)) {
            session.setAttribute("flagS", true);

            JSFunc.alertBack("제목은 비워둘 수 없습니다.", response);
            return null;
        }
        if(videoFiles.length <= 0 ){
            session.setAttribute("flagS", true);

            JSFunc.alertBack("영상은 필수입니다",response);
            return null;
        }
        if (Arrays.stream(videoFiles).anyMatch(MultipartFile::isEmpty)) {
            session.setAttribute("flagS", true);

            JSFunc.alertBack("영상은 필수입니다", response);
            return null;
        }

        response.setCharacterEncoding("UTF-8");
        for(int i=0; i<videoFiles.length; i++) {
            if(!videoFiles[i].isEmpty()) {
                 String fileName = videoFiles[i].getOriginalFilename();
                 String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                 String[] allowedExtensions = {"mp4", "webm"};
                 boolean extensionTest = Arrays.asList(allowedExtensions).contains(fileExtension);
                 if(!extensionTest) {
                     JSFunc.alertBack("동영상 파일만 업로드 가능합니다 (mp4, webm)",response);
                     return null;
                 }
                 long fileSize = videoFiles[i].getSize();
                 if(fileSize > 1048576 * 50) {
                     JSFunc.alertBack("파일 크기는 50MB 이하만 업로드 가능합니다",response);
                     return null;
                 }
            }
        }

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
        boolean hasNext = true;
        try{
            hasNext = sectionIdList.contains(sectionIdList.get(currentIndex+1));    // 다음 인덱스가 존재하는지 여부를 체크
        } catch (Exception e) {
            log.info(e); //사실상 여기가 끝나는 지점
            courseMapper.finishInsert(courseIdx);
            return "redirect:/teacher/course/list";
        }
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


    @GetMapping("/course/list")
    public String courseList(Model model, HttpSession session,
                             @RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(required = false) String searchValue,
                             @RequestParam(required = false) String sortType,
                             @RequestParam(required = false) String sortOrder) {
        String teacherId = (String) session.getAttribute("memberId");
        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCount = teacherMapper.totalCount(teacherId, "title", searchValue);
        Paging paging = new Paging(pageNo, 6, 5, totalCount, sortType, sortOrder);
        List<TeacherCourseDTO> list = teacherService.getMyCourse(teacherId, pageNo, 6, "title", searchValue, sortQuery);

        model.addAttribute("list",list);
        model.addAttribute("paging", paging);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/teacher/course/list");
        return "teacher/course/list";
    }

    @GetMapping("/course/modify")
    public String modify(Model model, int courseIdx, HttpSession session, HttpServletResponse response) {
        String teacherId = (String) session.getAttribute("memberId");
        CourseDTO courseDTO = courseSerivce.courseView1(courseIdx);
        response.setCharacterEncoding("UTF-8");
        if(courseDTO == null){
            JSFunc.alertBack("존재하지 않는 강좌입니다.",response);
            return null;
        }
        if(!courseDTO.getTeacherId().equals(teacherId)){
            JSFunc.alertBack("자신의 강좌만 수정할 수 있습니다.",response);
            return null;
        }

        log.info("courseDTO:{}",courseDTO);
        model.addAttribute("course", courseDTO);
        return "teacher/course/modify";
    }

    @PostMapping("/course/modify")
    public String modify(@Valid CourseMDTO courseMDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            model.addAttribute("course", courseMDTO);
            return "redirect:/teacher/course/modify?courseIdx="+courseMDTO.getIdx();
        }
        CourseDTO courseDTO = modelMapper.map(courseMDTO, CourseDTO.class);
        response.setCharacterEncoding("UTF-8");
        if(!courseDTO.getThumbnailFile().isEmpty()){
            String fileName = courseDTO.getThumbnailFile().getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();  // 확장자 소문자로 변환
            String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};  // 점 제외한 확장자만 나열
            boolean extensionTest = Arrays.asList(allowedExtensions).contains(fileExtension);

            long fileSize = courseDTO.getThumbnailFile().getSize();
            if(fileSize > 1048576) {
                JSFunc.alertBack("파일 크기는 1MB 이하만 업로드 가능합니다",response);
                return null;
            }
            if(!extensionTest) {
                JSFunc.alertBack("이미지 파일만 업로드 가능합니다 (jpg, jpeg, png)",response);
                return null;
            }
        }
        try {
            String filePath = CommonFileUtil.uploadFile(courseDTO.getThumbnailFile());
            courseDTO.setThumbnail(filePath);
            if(filePath != null){
               boolean delete = CommonFileUtil.deleteFile(courseSerivce.courseView1(courseDTO.getIdx()).getThumbnail());
               log.info("delete:{}",delete);
            }
        } catch (Exception e){
            log.error(e);
            log.info("[modify]>>파일 업로드 시 오류 발생");
            JSFunc.alertBack("파일 업로드 시 오류 발생. 다시 시도", response);
        }
        int result = courseSerivce.updateCourse(courseDTO);
        log.info("result : {}", result);
        log.info("courseDTO:{}",courseDTO);
        redirectAttributes.addAttribute("courseIdx", courseDTO.getIdx());
        return "redirect:/teacher/course/modify_s";
    }

    @GetMapping("/course/modify_s")
    public String modify_s(Model model, @RequestParam("courseIdx") int courseIdx, HttpSession session, HttpServletResponse response) {
        CourseDTO courseDTO = courseSerivce.courseView1(courseIdx);
        response.setCharacterEncoding("UTF-8");
        String teacherId = (String) session.getAttribute("memberId");
        if(courseDTO == null){
            JSFunc.alertBack("존재하지 않는 강좌입니다.",response);
            return null;
        }
        if(!courseDTO.getTeacherId().equals(teacherId)){
            JSFunc.alertBack("자신의 강좌만 수정할 수 있습니다.",response);
            return null;
        }
        //여기에 이제 섹션 정보 가져와서 뿌려줘야 함!
        List<SectionDTO> sectionList = sectionService.sectionList(courseDTO.getIdx());
        int sectionCount = sectionList.size();
        model.addAttribute("sectionCount", sectionCount);
        model.addAttribute("sectionList",sectionList);
        model.addAttribute("courseDTO", courseDTO);
        return "teacher/course/modify_s";
    }


    @PostMapping("/course/modify_s")
    public String modify_s(@RequestParam("sections") String[] sections,
                           @RequestParam("courseIdx") int courseIdx,
                           @RequestParam("sectionCount") int sectionCount,
                           @RequestParam(value = "sectionIdx") int[] sectionIdx,
                           HttpServletResponse response,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        response.setCharacterEncoding("UTF-8");
        if(sections.length <= 0 ){
            JSFunc.alertBack("섹션은 비워둘 수 없습니다.",response);
            return null;
        }
        if (Arrays.stream(sections).anyMatch(String::isEmpty)) {
            JSFunc.alertBack("섹션은 비워둘 수 없습니다.", response);
            return null;
        }


        for(int i=0;i<sectionCount;i++){
            String sectionTitle = sections[i];
            SectionDTO sectionDTO = SectionDTO.builder()
                            .idx(sectionIdx[i])
                            .section(sectionTitle)
                            .courseIdx(courseIdx)
                            .build();
            log.info("sectionDTO:{}",sectionDTO);
            int result = sectionService.updateSection(sectionDTO);
            if(result <= 0 ){
                JSFunc.alertBack("기존 섹션 업데이트 중 오류 발생", response);
                return null;
            }
        }
        for(int i=sectionCount;i<sections.length;i++){
            String sectionTitle = sections[i];
            SectionDTO sectionDTO = SectionDTO.builder()
                    .section(sectionTitle)
                    .courseIdx(courseIdx)
                    .build();
            log.info("sectionDTO:{}",sectionDTO);
            int result = sectionService.insertSection(sectionDTO);
            if(result <= 0 ){
                JSFunc.alertBack("섹션 추가 중 오류 발생", response);
                return null;
            }
        }

        redirectAttributes.addAttribute("courseIdx", courseIdx);
        return "redirect:/teacher/course/modify_l";
    }

    @GetMapping("/course/modify_l")
    public String modify_l(Model model, @RequestParam("courseIdx") int courseIdx, HttpSession session, HttpServletResponse response) {
        CourseDTO courseDTO = courseSerivce.courseView1(courseIdx);
        response.setCharacterEncoding("UTF-8");
        String teacherId = (String) session.getAttribute("memberId");
        if(courseDTO == null){
            JSFunc.alertBack("존재하지 않는 강좌입니다.",response);
            return null;
        }
        if(!courseDTO.getTeacherId().equals(teacherId)){
            JSFunc.alertBack("자신의 강좌만 수정할 수 있습니다.",response);
            return null;
        }
        List<SectionDTO> sectionDTOList = sectionService.sectionList(courseIdx);
        List<SectionWithLessonListDTO> sectionWithLessonListDTOList = new ArrayList<>();
        for(SectionDTO sectionDTO : sectionDTOList){
            int sectionIdx = sectionDTO.getIdx();
            List<LessonDTO> lessons = lessonService.getLessons(sectionIdx);
            SectionWithLessonListDTO sectionWithLessonListDTO = modelMapper.map(sectionDTO, SectionWithLessonListDTO.class);
            int lessonCount = lessons.size();
            sectionWithLessonListDTO.setLessonCount(lessonCount);
            sectionWithLessonListDTO.setLessons(lessons);
            sectionWithLessonListDTOList.add(sectionWithLessonListDTO);
        } //여기까지 했으면 이제 섹션과 해당 강의가 같이 말아짐

        log.info("sectionWithLessonListDTOList:{}",sectionWithLessonListDTOList);
        log.info("sectionListWithLesson : {}", sectionWithLessonListDTOList.get(0));
        model.addAttribute("sectionSize", sectionDTOList.size());
        model.addAttribute("sections", sectionWithLessonListDTOList.get(0));
        model.addAttribute("hasNextNext",true);
        return "teacher/course/modify_l";
    }

    @PostMapping("/course/modify_l")
    public String modify_l(@RequestParam("title") String[] titles,
                           @RequestParam("videoFile") MultipartFile[] videoFiles,
                           @RequestParam("sectionIdx") int sectionIdx,
                           @RequestParam("lessonCount") int lessonCount,
                           @RequestParam(value = "lessonIdx", required = false) int[] lessonIdx,
                           Model model,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        if(titles.length == 0){
            JSFunc.alertBack("제목은 비워둘 수 없습니다.",response);
            return null;
        }
        if (Arrays.stream(titles).anyMatch(String::isEmpty)) {
            JSFunc.alertBack("제목은 비워둘 수 없습니다.", response);
            return null;
        }
        if(videoFiles.length == 0){
            JSFunc.alertBack("영상은 필수입니다",response);
            return null;
        }
        if (Arrays.stream(videoFiles)
                .skip(lessonCount)  // lessonCount 이후의 파일들만 검사
                .anyMatch(MultipartFile::isEmpty)) {
            JSFunc.alertBack("영상은 필수입니다", response);
            return null;
        }

        for(int i=0; i<videoFiles.length; i++) {
            if(!videoFiles[i].isEmpty()) {
                String fileName = videoFiles[i].getOriginalFilename();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                String[] allowedExtensions = {"mp4", "webm"};
                boolean extensionTest = Arrays.asList(allowedExtensions).contains(fileExtension);
                if(!extensionTest) {
                    JSFunc.alertBack("동영상 파일만 업로드 가능합니다 (mp4, webm)",response);
                    return null;
                }
                long fileSize = videoFiles[i].getSize();
                if(fileSize > 1048576 * 50) {
                    JSFunc.alertBack("파일 크기는 50MB 이하만 업로드 가능합니다",response);
                    return null;
                }
            }
        }



        log.info("lessonCount:{}",lessonCount);
        log.info("title: {}:", Arrays.toString(titles));
        log.info("videoFiles: {}:", Arrays.toString(videoFiles));
        log.info("lessonIdx : {}", lessonIdx);
        //여기는 일단 이미 있는 강의들을 업데이트 쳐주는 공간
        String[] filePaths = new String[titles.length]; // 파일 전체를 저장해둘 수 있는 통임.
        for(int i=0;i<lessonCount;i++){ // 이미 존재하는 강의들만 돌면서 파일 업로드를 진행하고, "삭제하고" , 동영상 경로를 배열에 저장할 것
            //근데 여기서 삭제를 하려면, 해당 강의의 idx가 필요함? lessonIdx가 존재하는 친구들은 이미 있는 강의들.
            try {
                filePaths[i] = CommonFileUtil.uploadFile(videoFiles[i]);
            } catch (Exception e) {
                JSFunc.alertBack("영상 업로드 중 오류 발생: "+videoFiles[i],response);
                log.error(e);
                break;
            }
            try { //삭제 로직이 여기에 들어가야 함
                String videoPath = lessonMapper.getVideo(lessonIdx[i]);
                log.info("videoPath: {}", videoPath);
                if(filePaths[i] != null) {
                    CommonFileUtil.deleteFile(videoPath);
                }
            } catch (Exception e) {
                JSFunc.alertBack("기존 영상 삭제 중 오류 발생",response);
                log.error(e);
                break;
            }
        }
        log.info("filePaths: {}", Arrays.toString(filePaths));
        for(int i = 0;i<lessonCount;i++){
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setTitle(titles[i]);
            if(filePaths[i] != null) {
                lessonDTO.setVideo(filePaths[i]);
            }
            lessonDTO.setSectionIdx(sectionIdx);
            lessonDTO.setIdx(lessonIdx[i]);
            int result = lessonService.updateLesson(lessonDTO);
            if(result <= 0){
                JSFunc.alertBack("해당 강의 등록 중 오류 발생 : " + titles[i],response);
                break;
            }
        }
        //여기까지 했으면 이미 존재하는 강의의 업데이트가 완료됨

        //여기부터는 이제 새로운 거 등록

        for(int i=lessonCount;i<titles.length;i++){  // 이제 새로운 강의들을 등록함
            try {
                filePaths[i] = CommonFileUtil.uploadFile(videoFiles[i]);
            } catch (Exception e) {
                JSFunc.alertBack("영상 업로드 중 오류 발생: "+videoFiles[i],response);
                log.error(e);
                break;
            }
        }
        log.info("추가된 filePaths: {}", Arrays.toString(filePaths));
        for(int i=lessonCount;i<titles.length;i++){
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setTitle(titles[i]);
            lessonDTO.setVideo(filePaths[i]); //파일 무조건 입력해야 함.
            lessonDTO.setSectionIdx(sectionIdx);
            log.info("lessonDTO:{}",lessonDTO);
            int result = lessonService.insertLesson(lessonDTO);
            log.info("result:{}",result);
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
        boolean hasNext = true;
        try{
            hasNext = sectionIdList.contains(sectionIdList.get(currentIndex+1));    // 다음 인덱스가 존재하는지 여부를 체크
        } catch (Exception e) {
            log.info(e); //사실상 여기가 끝나는 지점
            courseMapper.finishInsert(courseIdx);
            return "redirect:/teacher/course/list";
        }
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

        List<SectionDTO> sectionDTOList = sectionService.sectionList(courseIdx); // 강좌 인덱스로 섹션의 리스트를 가져옴.
        List<SectionWithLessonListDTO> sectionWithLessonListDTOList = new ArrayList<>(); // 강의 리스트가 포함된 섹션 리스트를 생성
        for(SectionDTO sectionDTO : sectionDTOList){ // 돌면서 이제 sectionDTO에 리스트 정보를 넣어주어야 함.
            int sectionIdx1 = sectionDTO.getIdx();
            log.info("sectionIdx1:{}",sectionIdx1);
            List<LessonDTO> lessons = lessonService.getLessons(sectionIdx1);
            log.info("lessons:{}",lessons);
            SectionWithLessonListDTO sectionWithLessonListDTO = modelMapper.map(sectionDTO, SectionWithLessonListDTO.class);
            log.info("sectionWithLessonListDTO:{}",sectionWithLessonListDTO);
            lessonCount = lessons.size();
            sectionWithLessonListDTO.setLessonCount(lessonCount);
            sectionWithLessonListDTO.setLessons(lessons);
            sectionWithLessonListDTOList.add(sectionWithLessonListDTO);
        } //여기까지 했으면 이제 섹션과 해당 강의가 같이 말아짐


        model.addAttribute("sections", sectionWithLessonListDTOList.get(currentIndex+1)); // 여기가 다음 번의 섹션을 넘기는 용도
        model.addAttribute("hasNextNext", hasNextNext);
        return "teacher/course/modify_l";
    }


    @GetMapping("/list")
    public String teacherList(Model model,
                              @RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(required = false) String searchCategory,
                              @RequestParam(required = false) String searchValue,
                              @RequestParam(required = false) String sortType,
                              @RequestParam(required = false) String sortOrder){
        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = teacherService.teacherTotalCnt(searchCategory, searchValue, "T", "Y");
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<MemberDTO> teacherList = teacherService.selectAllTeacher(pageNo, 10, searchCategory, searchValue, sortQuery, "T", "Y");
        System.out.println(teacherList.size());
        model.addAttribute("teacherList", teacherList);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/teacher/list");
        return "teacher/list";
    }

    @GetMapping("/viewIntroduce")
    public String teacherView(Model model, @RequestParam String memberId){
        if(memberId != null && !memberId.isEmpty()){
            MemberDTO memberInfo = memberService.selectMemberInfo(memberId);
            model.addAttribute("info", memberInfo);
            return "teacher/teacherIntroduce";
        }else{
            log.info("회원 아이디 없음");
            return null;
        }
    }

//    @GetMapping("/viewCourseList")
//    public String CourseListView(Model model, @RequestParam String memberId){
//        if(memberId != null && !memberId.isEmpty()){
//            MemberDTO info = memberService.selectMemberInfo(memberId);
//            model.addAttribute("info", info);
//            List<CourseDTO> courseInfo = courseSerivce.courseList(memberId);
//            model.addAttribute("courseInfo", courseInfo);
//        }else{
//            log.info("회원 아이디 없음");
//            return null;
//        }
//        return "teacher/teacherCourseList";
//    }

    @GetMapping("/viewCourseList")
    public String CourseListView(Model model, @RequestParam(required = false) String memberId,
                                 @RequestParam(defaultValue = "1") int pageNo){
        if(memberId != null && !memberId.isEmpty()){
            MemberDTO info = memberService.selectMemberInfo(memberId);
            model.addAttribute("info", info);
            int totalCnt = reviewService.courseCntByTeacher(memberId);
            System.out.println("total: " + totalCnt);
            Paging paging = new Paging(pageNo, 10, 5, totalCnt, null, null);
            List<CourseDTO> courseInfo = courseSerivce.courseList(pageNo, 10,memberId);
            model.addAttribute("courseInfo", courseInfo);
            model.addAttribute("paging", paging);
            model.addAttribute("uri", "/teacher/viewCourseList");
        }else{
            log.info("회원 아이디 없음");
            return null;
        }
        return "teacher/teacherCourseList";
    }


    @GetMapping("/viewReview")
    public String CourseReviewView(Model model,
                                   @RequestParam String memberId,
                                   @RequestParam(defaultValue = "1") int pageNo) {
        if (memberId != null && !memberId.isEmpty()) {
            MemberDTO info = memberService.selectMemberInfo(memberId);
            model.addAttribute("info", info);
            int totalCnt = reviewService.reviewCntByTeacher(memberId);
            Paging paging = new Paging(pageNo, 10, 5, totalCnt, null, null);
            List<ReviewListDTO> reviews = reviewService.reviewListByTeacher(pageNo, 10, memberId);

            if (reviews != null && !reviews.isEmpty()) {
                log.info("총 {}개의 리뷰가 조회되었습니다.", reviews.size());
                for (ReviewListDTO review : reviews) {
                    System.out.println(review.getContent());
                    System.out.println(review.getIdx());
                }
            } else {
                log.info("리뷰 데이터가 없습니다.");
            }

            model.addAttribute("reviews", reviews);
            model.addAttribute("paging", paging);
            model.addAttribute("uri", "/teacher/viewReview");
            return "teacher/teacherReview";
        } else {
            log.info("회원 아이디 없음");
            return null;
        }
    }

    @GetMapping("/modifyInfo")
    public String accountInfo(Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId != null) {
            MemberDTO memberInfo = myPageService.accountInfo(memberId);
            model.addAttribute("memberInfo", memberInfo);
            return "teacher/teacherModify";
        }
        return "redirect:/";
    }

    @GetMapping("/pwChangeTeacher")
    public String pwChangeTeacher() {
        return "teacher/teacherModifyPw";
    }

    @GetMapping("/course/delete")
    public void requestDelete(
            @RequestParam("courseIdx") Long courseIdx, HttpServletResponse response) {
    response.setCharacterEncoding("UTF-8");
        log.info("courseIdx:{}",courseIdx);

        // 삭제 요청 로직 처리
        boolean success = courseMapper.processDeleteRequest(courseIdx);

        if(success){
            JSFunc.alertLocation("삭제 요청 완료", "/teacher/course/list",response);
        } else {
            JSFunc.alertLocation("삭제 요청 실패","/teacher/course/list",response);
        }
    }
    @GetMapping("/course/deleteN")
    public void requestDeleteN(
            @RequestParam("courseIdx") Long courseIdx, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        log.info("courseIdx:{}",courseIdx);

        // 삭제 요청 로직 처리
        boolean success = courseMapper.processDeleteRequestN(courseIdx);

        if(success){
            JSFunc.alertLocation("삭제 취소 요청 완료", "/teacher/course/list",response);
        } else {
            JSFunc.alertLocation("삭제 취소 요청 실패","/teacher/course/list",response);
        }
    }
}
