package inflearn_clone.springboot.service.course;


import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import inflearn_clone.springboot.service.course.CourseSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<CourseDTO> courseList() {
        return null;
    }

    @Override
    public CourseDTO courseView1(int idx) {
            CourseVO vo = courseMapper.courseView1(idx);
            CourseDTO courseInfo = modelMapper.map(vo, CourseDTO.class);
            return courseInfo;
    }

    //teacherId로 존재하는 강의가 있는지 확인하기
    @Override
    public List<Integer> selectCourseByMemberId(String memberId) {
        return courseMapper.selectCourseByMemberId(memberId);
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
    public List<CourseDTO> allCourseList(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);

        List<CourseVO> voList =  courseMapper.allCourseList(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, CourseDTO.class)).collect(Collectors.toList());
    }

}
