package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.admin.AdminLoginDTO;
import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.course.CourseTotalDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.member.LeaveReasonDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
import inflearn_clone.springboot.dto.order.OrderRefundDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.dto.section.SectionWithLessonListDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import inflearn_clone.springboot.mappers.ReviewMapper;
import inflearn_clone.springboot.mappers.SectionMapper;
import inflearn_clone.springboot.mappers.TeacherMapper;
import inflearn_clone.springboot.service.admin.AdminServiceIf;
import inflearn_clone.springboot.service.course.CourseSerivce;
import inflearn_clone.springboot.service.admin.NoticeServiceIf;
import inflearn_clone.springboot.service.lesson.LessonServiceIf;
import inflearn_clone.springboot.service.member.MemberServiceIf;
import inflearn_clone.springboot.service.myPage.MyPageService;
import inflearn_clone.springboot.service.order.OrderService;
import inflearn_clone.springboot.service.review.ReviewService;
import inflearn_clone.springboot.service.section.SectionServiceIf;
import inflearn_clone.springboot.service.teacher.TeacherServiceIf;
import inflearn_clone.springboot.utils.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static inflearn_clone.springboot.utils.QueryUtil.generateSortQuery;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final MemberServiceIf memberService;
    private final NoticeServiceIf noticeService;
    private final CourseSerivce courseSerivce;
    private final OrderService orderService;
    private final SectionServiceIf sectionService;
    private final SectionMapper sectionMapper;
    private final LessonServiceIf lessonService;
    private final CourseMapper courseMapper;
    private final ModelMapper modelMapper;

    /**
     * 관리자 로그인 페이지 이동
     *
     */
    @GetMapping("/login")
    public String loginPage(){
        return "admin/login";
    }

    /**
     * 대시보드
     * 강사 탈퇴 요청 수, 회원분류(활성, 비활성, 탈퇴), 강의관리(과목 별 강의 수)
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletResponse response, HttpSession session){
        String adminId = (String) session.getAttribute("adminId");
        if (adminId == null) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("로그인이 필요합니다", response);
            throw new IllegalStateException("로그인이 필요합니다."); // 예외 처리
        }

        // 강사 탈퇴 요청 수 조회
        int teacherTotalCnt = memberService.teacherRequestTotalCnt(null, null, "T");
        model.addAttribute("teacherRequestTotalCnt", teacherTotalCnt);

        // 일반 회원 상태 별 조회
        int statusYTotalCnt = memberService.memberStatusTotalCnt("Y", "S", LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        int statusNTotalCnt = memberService.memberStatusTotalCnt("N", "S", LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        int statusDTotalCnt = memberService.memberStatusTotalCnt("D", "S", LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        model.addAttribute("statusYTotalCnt", statusYTotalCnt);
        model.addAttribute("statusNTotalCnt", statusNTotalCnt);
        model.addAttribute("statusDTotalCnt", statusDTotalCnt);

        // 강의 관리(과목 별 강의 수)
        int categoryDTotalCnt =  courseSerivce.categoryTotalCnt("D");
        int categoryATotalCnt =  courseSerivce.categoryTotalCnt("A");
        int categoryMTotalCnt =  courseSerivce.categoryTotalCnt("M");
        int categoryHTotalCnt =  courseSerivce.categoryTotalCnt("H");
        int categoryCTotalCnt =  courseSerivce.categoryTotalCnt("C");
        model.addAttribute("categoryDTotalCnt", categoryDTotalCnt);
        model.addAttribute("categoryATotalCnt", categoryATotalCnt);
        model.addAttribute("categoryMTotalCnt", categoryMTotalCnt);
        model.addAttribute("categoryHTotalCnt", categoryHTotalCnt);
        model.addAttribute("categoryCTotalCnt", categoryCTotalCnt);


        //최근에 작성된 공지사항
        int totalCnt = noticeService.noticeTotalCnt(null, null);
        List<BbsDTO> notice =  noticeService.list(1, 3, null, null, null);
        model.addAttribute("notice", notice);
        return "admin/dashboard";
    }

    /**
     * 학생 회원 리스트
     *
     */


    @GetMapping("/member/sList")
    public String studentlist(Model model,
                       HttpServletResponse response,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder,
                       @RequestParam(required = false) String status){
        response.setCharacterEncoding("utf-8");
        if (!ValidateList.validateMemberListParameters(pageNo, searchCategory, searchValue, sortType, sortOrder, response)) {
            return null;
        }
        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = memberService.memberTotalCnt(searchCategory, searchValue, "S", "");
        log.info("Member list totalCnt" + totalCnt); // 100
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<MemberDTO> members =  memberService.selectAllMember(pageNo, 10, searchCategory, searchValue, sortQuery, "S", "");
        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/admin/member/sList");
        return "/admin/member/stdList";
    }

    /**
     * 강사 회원 목록
     *
     */
    @GetMapping("/member/tList")
    public String list_1(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder,
                         @RequestParam(required = false) String status){
        String sortQuery = generateSortQuery(sortType, sortOrder);

        int totalCnt = memberService.memberTotalCnt(searchCategory, searchValue, "T", "");
        log.info("Member list totalCnt" + totalCnt);
        log.info("searchCategory" + searchCategory);
        log.info("searchValue" + searchValue);
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<MemberDTO> members =  memberService.selectAllMember(pageNo, 10, searchCategory, searchValue, sortQuery, "T", "");
        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/admin/member/tList");
        return "admin/member/teacherList";
    }

    /**
     * 강사 탈퇴 요청 회원 목록
     *
     */
    @GetMapping("/member/teacherRequestList")
    public String teacherlist(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder){
        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = memberService.teacherRequestTotalCnt(searchCategory, searchValue, "T");
        log.info("Member totalCnt" + totalCnt);
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<MemberDTO> members =  memberService.selectTeacherRequest(pageNo, 10, searchCategory, searchValue, sortQuery);
        model.addAttribute("members", members);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/admin/member/teacherRequestList");
        return "admin/member/teacherRequestList";
    }

    /**
     * 회원 조회
     *
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

    /**
     * 회원 수정 폼 전달
     *
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

    /**
     * 회원 수정
     * 회원 수정 후 회원 상세 페이지로 이동
     */
    @PostMapping("/member/modify")
    public String modifyPost(@ModelAttribute MemberDTO dto){
        if(dto.getMemberType().equals('T')){
            boolean result = memberService.modifyMemberInfo(dto);
            if(result){
                return "redirect:/admin/member/view?memberId=" + dto.getMemberId();
            }
        }else{
            boolean result = memberService.modifyMemberInfo(dto);
            if(result){
                return "redirect:/admin/member/view?memberId=" + dto.getMemberId();
            }
        }

        return null;
    }

    /**
     * 회원 탈퇴 사유 조회
     *
     */
    @GetMapping("/member/leaveReasonView")
    public String leaveReasonView(Model model, String memberId){
        LeaveReasonDTO info = memberService.leaveReasonView(memberId);
        if(info != null){
            model.addAttribute("info", info);
            return "admin/member/leaveReason";
        }else{
            log.info("조회된 내용이 없습니다."); // 로직추가할것
        }
        return null;
    }

    /**
     * 강의 목록 작성 중
     */
    @GetMapping("/course/list")
    public String courseList(Model model,
                             @RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "false") String searchCategory,
                             @RequestParam(defaultValue = "false") String searchValue,
                             @RequestParam(required = false) String sortType,
                             @RequestParam(required = false) String sortOrder){
        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = courseSerivce.allCourseListTotalCnt(searchCategory, searchValue, "Y");
        log.info("Course list totalCnt" + totalCnt);
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<CourseDTO> courses =  courseSerivce.allCourseList(pageNo, 10, searchCategory, searchValue, sortQuery);
        model.addAttribute("courses", courses);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("uri", "/admin/course/list");
        return "/admin/course/courseList";

    }

    /**
     * 강좌 조회
     */

    @GetMapping("/course/view")
    public String courserView(Model model, @RequestParam int idx){
        CourseDTO courseInfo = courseSerivce.courseView1(idx);
        model.addAttribute("info", courseInfo);
        if(courseInfo != null){
            return "/admin/course/view";
        }else{
            log.info("강좌 정보 없음");
        }
        return null;
    }

    /**
     * 강좌 삭제
     *
     */
    @GetMapping("/course/delete")
    public String deleteCourse(@RequestParam String idx, @RequestParam String memberId){
        List<CourseDTO> list = courseSerivce.selectCourseByMemberId(memberId);
        if(list.size() > 0){
            // 공지사항 자동 등록 로직
            int insertNotice = noticeService.autoInsert(memberId, list);
            if(insertNotice > 0){
                memberService.deleteMemberInfo(memberId);
                return "강의 삭제 예약 완료";
            }else{
                return "예약 설정 중 오류가 발생했습니다.";
            }
        }else{
            // 회원탈퇴 로직 추가
            memberService.deleteMemberInfo(memberId);
            return "운영 중인 강좌가 없습니다.";
        }
    }

    /**
     * 회원 탈퇴
     * 회원이 탈퇴 가능한 상태인지 확인
     * 환불 처리 절차가 필요한 경우라면 즉시 탈퇴 처리가 아닌 유예 상태로 변경
     */
//    @GetMapping("/member/delete")
//    public String memberDelete(@RequestParam String memberId, @RequestParam String memberType){
//        if(memberType.equals("T")){
//            List<CourseDTO> list = courseSerivce.selectCourseByMemberId(memberId);
//            //System.out.println(result.size());
//            if(list.size() > 0){
//                // 공지사항 자동 등록 로직
//                int insertNotice = noticeService.autoInsert(memberId, list);
//                if(insertNotice > 0){
//                    // 공지사항 등록 이후 30일 뒤 강좌 삭제되도록 처리해야함 (즉 예약 처리)
//                    // 예약 로직이 들어가야함
//                    memberService.deleteMemberInfo(memberId);
//                    return "강의 삭제 예약 완료";
//
//                }else{
//                    return "예약 설정 중 오류가 발생했습니다.";
//                }
//            }else{
//                // 회원탈퇴 로직 추가
//                memberService.deleteMemberInfo(memberId);
//                return "운영 중인 강좌가 없습니다.";
//            }
//        }else{
//            memberService.deleteMemberInfo(memberId);
//            return "학생 로직 처리";
//        }
//    }
    @GetMapping("/member/delete")
    public String memberDelete(@RequestParam String memberId,
                               @RequestParam String memberType,
                               HttpServletResponse response){
        if(memberType.equals("T")){
            List<CourseDTO> list = courseSerivce.selectCourseByMemberId(memberId);
            if(list.size() > 0){
                // 공지사항 자동 등록 로직
                int insertNotice = noticeService.autoInsert(memberId, list);
                if(insertNotice > 0){
                    memberService.deleteMemberInfo(memberId);
                    JSFunc.alertBack("공지사항이 등록되었습니다.", response);
                    return "";

                }else{
                    return "예약 설정 중 오류가 발생했습니다.";
                }
            }else{
                // 회원탈퇴 로직 추가
                memberService.deleteMemberInfo(memberId);
                return "운영 중인 강좌가 없습니다.";
            }
        }else{
            memberService.deleteMemberInfo(memberId);
            return "학생 로직 처리";
        }
    }

    /**
     * 환불자 명단 조회
     *
     */
    @GetMapping("/course/refundList")
    public String selectRefundList(Model model, @RequestParam int idx){
        List<OrderRefundDTO> orderList = orderService.refundByDeleteCourse(idx);
        model.addAttribute("list", orderList);
        return "admin/course/refundList";
    }

    @GetMapping("/course/insert")
    public String insertCourse() {
        return "admin/course/insert";
    }

    @PostMapping("/course/insert")
    public String insert(CourseDTO courseDTO, RedirectAttributes redirectAttributes, HttpServletResponse response, HttpSession session) {
        String adminId = (String) session.getAttribute("memberId");
        courseDTO.setTeacherId(adminId);
        try {
            String filePath = CommonFileUtil.uploadFile(courseDTO.getThumbnailFile());
            courseDTO.setThumbnail(filePath);
        } catch( IOException e){
            log.error(e);
        }
        int result = courseSerivce.insertCourse(courseDTO);
        if(result > 0){
            return "redirect:/admin/course/insert_s";
        } else {
            JSFunc.alertBack("강좌 등록에 실패했습니다. 다시 시도해주세요",response);
            return null;
        }
    }

    @GetMapping("/course/insert_s")
    public String insert_s(Model model, HttpSession session) {
        String adminId = (String) session.getAttribute("memberId");
        CourseDTO courseDTO = courseSerivce.viewMyLastCourse(adminId);
        model.addAttribute("courseDTO", courseDTO);
        return "admin/course/insert_s";
    }
    @GetMapping("/course/insert_ss")
    public String insert_ss(Model model, HttpSession session, @RequestParam int courseIdx,HttpServletResponse response) {
        String teacherId = (String) session.getAttribute("memberId");
        CourseTotalDTO courseDTO = courseSerivce.courseView
                (courseIdx);
        if(!Objects.equals(courseDTO.getTeacherId(), teacherId)){
            JSFunc.alertBack("자신의 강좌만 등록 가능합니다.",response);
            return null;
        }

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
            return "redirect:/admin/course/insert_l";
        } catch (Exception e) {
            log.error(e);
            log.info("catch됨");
            JSFunc.alertBack("섹션등록실패",response);
            return null;
        }
    }

    @GetMapping("/course/insert_l")
    public String insert_l(Model model, HttpSession session) {
        String adminId = (String) session.getAttribute("memberId");
        int courseIdx = courseSerivce.viewMyLastCourse(adminId).getIdx();
        List<SectionDTO> sectionDTOList = sectionService.sectionList(courseIdx);
        model.addAttribute("sectionSize", sectionDTOList.size());
        model.addAttribute("section", sectionDTOList.get(0));
        model.addAttribute("hasNextNext",true);
        return "admin/course/insert_l";
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
        boolean hasNext = true;
        try{
            hasNext = sectionIdList.contains(sectionIdList.get(currentIndex+1));    // 다음 인덱스가 존재하는지 여부를 체크
        } catch (Exception e) {
            log.info(e); //사실상 여기가 끝나는 지점
            courseMapper.finishInsert(courseIdx);
            return "redirect:/admin/course/list";
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
        return "admin/course/insert_l";
    }


    @GetMapping("/course/modify")
    public String modify(Model model, int courseIdx){
        CourseDTO courseDTO = courseSerivce.courseView1(courseIdx);
        log.info("courseDTO:{}",courseDTO);
        model.addAttribute("course", courseDTO);
        return "admin/course/modify";
    }

    @PostMapping("/course/modify")
    public String modify(CourseDTO courseDTO, Model model, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        try {
            String filePath = CommonFileUtil.uploadFile(courseDTO.getThumbnailFile());
            courseDTO.setThumbnail(filePath);
        } catch (Exception e){
            log.error(e);
            log.info("[modify]>>파일 업로드 시 오류 발생");
            JSFunc.alertBack("파일 업로드 시 오류 발생. 다시 시도", response);
        }
        int result = courseSerivce.updateCourse(courseDTO);
        log.info("result : {}", result);
        log.info("courseDTO:{}",courseDTO);
        redirectAttributes.addAttribute("courseIdx", courseDTO.getIdx());
        return "redirect:/admin/course/modify_s";
    }

    @GetMapping("/course/modify_s")
    public String modify_s(Model model, @RequestParam("courseIdx") int courseIdx){
        CourseDTO courseDTO = courseSerivce.courseView1(courseIdx);
        //여기에 이제 섹션 정보 가져와서 뿌려줘야 함!
        List<SectionDTO> sectionList = sectionService.sectionList(courseDTO.getIdx());
        int sectionCount = sectionList.size();
        model.addAttribute("sectionCount", sectionCount);
        model.addAttribute("sectionList",sectionList);
        model.addAttribute("courseDTO", courseDTO);
        return "admin/course/modify_s";
    }


    @PostMapping("/course/modify_s")
    public String modify_s(@RequestParam("sections") String[] sections,
                           @RequestParam("courseIdx") int courseIdx,
                           @RequestParam("sectionCount") int sectionCount,
                           @RequestParam(value = "sectionIdx") int[] sectionIdx,
                           HttpServletResponse response,
                           RedirectAttributes redirectAttributes) {
        System.out.println("sectionIdx: " + Arrays.toString(sectionIdx));
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
        return "redirect:/admin/course/modify_l";
    }
    @GetMapping("/course/modify_ll")
    public String modify_ll(Model model, @RequestParam("courseIdx") int courseIdx){
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

        model.addAttribute("sectionSize", sectionDTOList.size());
        log.info("sectionWithLessonListDTOList:{}",sectionWithLessonListDTOList);
//        log.info("sectionListWithLesson : {}", sectionWithLessonListDTOList.get(0));
        model.addAttribute("sections", sectionWithLessonListDTOList.get(0));
        model.addAttribute("hasNextNext",true);
        return "admin/course/modify_l";
    }

    @GetMapping("/course/modify_l")
    public String modify_l(Model model, @RequestParam("courseIdx") int courseIdx){
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
        return "admin/course/modify_l";
    }

    @PostMapping("/course/modify_l")
    public String modify_l(@RequestParam("title") String[] titles,
                           @RequestParam("videoFile") MultipartFile[] videoFiles,
                           @RequestParam("sectionIdx") int sectionIdx,
                           @RequestParam("lessonCount") int lessonCount,
                           @RequestParam(value = "lessonIdx", required = false) int[] lessonIdx,
                           Model model,HttpServletResponse response){
        log.info("lessonCount:{}",lessonCount);
        log.info("title: {}:", Arrays.toString(titles));
        log.info("videoFiles: {}:", Arrays.toString(videoFiles));
        log.info("lessonIdx : {}", lessonIdx);
        //여기는 일단 이미 있는 강의들을 업데이트 쳐주는 공간
        String[] filePaths = new String[titles.length]; // 파일 전체를 저장해둘 수 있는 통임.
        for(int i=0;i<lessonCount;i++){ // 이미 존재하는 강의들만 돌면서 파일 업로드를 진행하고, "삭제하고" , 동영상 경로를 배열에 저장할 것
            try {
                filePaths[i] = CommonFileUtil.uploadFile(videoFiles[i]);
            } catch (Exception e) {
                JSFunc.alertBack("영상 업로드 중 오류 발생: "+videoFiles[i],response);
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
            return "redirect:/admin/course/list";
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
        return "admin/course/modify_l";
    }
    @PostMapping("/course/modify_ll")
    public String modify_ll(@RequestParam("sectionIdx") int sectionIdx, Model model){

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
            return "redirect:/admin/course/list";
        }
        boolean hasNextNext = false;
        try {
            hasNextNext = sectionIdList.contains(sectionIdList.get(currentIndex + 2));
        } catch (Exception e) {
            log.error(e);
        }
        log.info("hasNextNext:{}",hasNextNext);
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
            int lessonCount = lessons.size();
            sectionWithLessonListDTO.setLessonCount(lessonCount);
            sectionWithLessonListDTO.setLessons(lessons);
            sectionWithLessonListDTOList.add(sectionWithLessonListDTO);
        } //여기까지 했으면 이제 섹션과 해당 강의가 같이 말아짐
        model.addAttribute("sections", sectionWithLessonListDTOList.get(currentIndex+1)); // 여기가 다음 번의 섹션을 넘기는 용도
        model.addAttribute("hasNextNext", hasNextNext);


        return "admin/course/modify_l";

    }



    //여기부터 아래는 인규가 작업한 부분입니다.
    // [관리자 공지사항]
    // 2024.11.28 송수미 수정

    // 11261051 --> validation 아직 안 됨
    @GetMapping("notice/insert")
    public String insert() {
        return "admin/notice/insert";
    }

    @PostMapping("notice/insert")
    public String insert(BbsDTO bbsDTO, HttpServletResponse response, HttpSession session) {

        String adminId = (String) session.getAttribute("adminId");
        if (adminId == null) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertBack("로그인이 필요합니다", response);
            throw new IllegalStateException("로그인이 필요합니다."); // 예외 처리
        }
        bbsDTO.setWriterId(adminId); //아직 세션 없음

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
    public String list(Model model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(required = false) String searchCategory,
                       @RequestParam(required = false) String searchValue,
                       @RequestParam(required = false) String sortType,
                       @RequestParam(required = false) String sortOrder) {

        String sortQuery = generateSortQuery(sortType, sortOrder);
        int totalCnt = noticeService.noticeTotalCnt(searchCategory, searchValue);
        Paging paging = new Paging(pageNo, 10, 5, totalCnt, sortType, sortOrder);
        List<BbsDTO> notice =  noticeService.list(pageNo, 10, searchCategory, searchValue, sortQuery);
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
        model.addAttribute("uri", "/admin/notice/list");
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
