package inflearn_clone.springboot.service.course;
import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.LessonVO;
import inflearn_clone.springboot.domain.ReviewVO;
import inflearn_clone.springboot.domain.SectionVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.course.CourseTotalDTO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.review.ReviewListDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import inflearn_clone.springboot.utils.CategoryMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseSerivce {

    private final CourseMapper courseMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<CourseDTO> courseList(int pageNo, int pageSize, String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("memberId", memberId);
        List<CourseVO> voList =  courseMapper.courseList(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, CourseDTO.class)).collect(Collectors.toList());
    }
    @Override
    public CourseTotalDTO courseView(int idx) {
        return courseMapper.courseView(idx);
    }
    @Override
    public CourseDTO courseView1(int idx) {
        CourseVO vo = courseMapper.courseView1(idx);
        if(vo == null) {
            return null;
        }
        CourseDTO courseInfo = modelMapper.map(vo, CourseDTO.class);
        return courseInfo;
    }
    //teacherId로 존재하는 강의가 있는지 확인하기
    @Override
    public List<CourseDTO> selectCourseByMemberId(String memberId) {
        List<CourseVO> voList = courseMapper.selectCourseByMemberId(memberId);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, CourseDTO.class))
                .collect(Collectors.toList());

    }
    // 특정 teacherId의 강의 삭제하기 (지금 필요없음)
    @Override
    public boolean deleteCourseByMemberId(String memberId) {
        return courseMapper.deleteCourseByMemberId(memberId);
    }

    @Override
    public boolean updateCourseStatusToDeleted(LocalDateTime now) {
        List<Integer> coursesToDelete = courseMapper.findCoursesToDeleted(now);
        if (coursesToDelete.isEmpty()) {
            log.info("삭제 예정 강좌가 없습니다.");
            return false;
        }

        log.info("삭제 예정 강좌: " + coursesToDelete);

        int updatedRows = courseMapper.updateCourseStatusToDeleted(now);
        if(updatedRows > 0){
            log.info(updatedRows + "개의 강좌가 삭제 상태로 변경되었습니다.");
            return true;
        }else{
            log.info("강좌가 삭제 상태로 변경되지 않았습니다.");
            return false;
        }
    }

    @Override
    public boolean deleteCourese(String idx) {
        boolean result = courseMapper.deleteCourse(idx);
        if(result){
            log.info("강좌가 삭제되었습니다.");
            return true;
        }else{
            log.info("강좌 삭제 실패하였습니다.");
            return false;
        }
    }

    @Override
    public int categoryTotalCnt(String category) {
        return courseMapper.categoryTotalCnt(category);
    }

    @Override
    public int allCourseListTotalCnt(String searchCategory, String searchValue, String status) {
        return courseMapper.allCourseListTotalCnt(searchCategory, searchValue, status);
    }

    @Override
    public List<CourseDTO> allCourseList(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);
        map.put("status", status);

        List<CourseVO> voList =  courseMapper.allCourseList(map);
        return voList.stream()
            .map(vo -> modelMapper.map(vo, CourseDTO.class)).collect(Collectors.toList());
    }
    @Override
    public List<CourseTotalDTO> getCourses(int pageNo, int pageSize, List<String> categoryCodes, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("categoryCodes", categoryCodes);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);

        return courseMapper.selectAllCourse(map);
    }

    @Override
    public int getTotalCourses(List<String> categoryCodes, String searchValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryCodes", categoryCodes);
        map.put("searchValue", searchValue);
        return courseMapper.courseTotalCnt(map);
    }


    @Override
    public int updateCourse(CourseDTO courseDTO) {
        CourseVO courseVO = modelMapper.map(courseDTO, CourseVO.class);
        return courseMapper.updateCourse(courseVO);
    }

    @Override
    public int insertCourse(CourseDTO courseDTO) {
        CourseVO courseVO = modelMapper.map(courseDTO, CourseVO.class);
        return courseMapper.insertCourse(courseVO);
    }

    @Override
    public CourseDTO viewMyLastCourse(String memberId) {
        return modelMapper.map(courseMapper.viewMyLastCourse(memberId), CourseDTO.class);
    }

    @Override
    public CourseDTO curriculum(int idx) {
        CourseVO course = courseMapper.selectCourse(idx);

        //섹션 정보 가져오기
        List<SectionVO> sections = courseMapper.selectSection(idx);

        //섹션별 강의 정보 가져오기
        List<Integer> sectionIdx = sections.stream()
                .map(SectionVO::getIdx)
                .collect(Collectors.toList());
        List<LessonVO> lessons = courseMapper.selectLesson(sectionIdx);

        //섹션과 강의 조합
//        Map<Integer, List<LessonVO>> lessonMap = lessons.stream()
//                .collect(Collectors.groupingBy(LessonVO::getSectionIdx));
//        sections.forEach(section -> section.setLessons(lessonMap.get(section.getIdx())));
        return null;
    }

    @Override
    public CourseDTO selectCourse(int courseIdx) {
        CourseVO courseVO = courseMapper.selectCourse(courseIdx);
        if (courseVO != null) {
            return modelMapper.map(courseVO, CourseDTO.class);
        }
        return null;
    }

    @Override
    public List<SectionDTO> selectSection(int courseIdx) {
        List<SectionVO> voList =  courseMapper.selectSection(courseIdx);
        return voList.stream().map(vo -> modelMapper.map(vo, SectionDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> selectLesson(List<Integer> sectionIdx) {
        List<LessonVO> voList =  courseMapper.selectLesson(sectionIdx);
        return voList.stream().map(vo -> modelMapper.map(vo, LessonDTO.class)).collect(Collectors.toList());
    }


}
