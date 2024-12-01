package inflearn_clone.springboot.service.teacher;

import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.course.TeacherCourseDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import inflearn_clone.springboot.mappers.MemberMapper;
import inflearn_clone.springboot.mappers.TeacherMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TeacherServiceImpl implements TeacherServiceIf{

    private final ModelMapper modelMapper;
    private final TeacherMapper teacherMapper;
    private final MemberMapper memberMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<TeacherCourseDTO> getMyCourse(String teacherId, int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);
        map.put("teacherId", teacherId);


        List<CourseDTO> courseList = teacherMapper.getMyCourseList(map);

        List<TeacherCourseDTO> list = new ArrayList<>();
        int lessonCnt = 0;
        int sectionCnt = 0;
        for (CourseDTO course : courseList) {
            TeacherCourseDTO teacherCourseDTO = modelMapper.map(course, TeacherCourseDTO.class);
            int courseIdx = course.getIdx();
            teacherCourseDTO.setIdx(courseIdx);
            lessonCnt = teacherMapper.lessonCount(teacherId, courseIdx);
            sectionCnt = teacherMapper.sectionCount(teacherId, courseIdx);
            teacherCourseDTO.setLessonCnt(lessonCnt);
            teacherCourseDTO.setSectionCnt(sectionCnt);
            list.add(teacherCourseDTO);
        }
        return list;
    }
    @Override
    public int teacherTotalCnt(String searchCategory, String searchValue, String memberType, String status) {
        return memberMapper.memberTotalCnt(searchCategory, searchValue, memberType, status);
    }

    @Override
    public List<MemberDTO> selectAllTeacher(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery, String memberType, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo -1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);
        map.put("memberType", memberType);
        map.put("status", status);
        List<MemberVO> voList =  memberMapper.selectAllMember(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class)).collect(Collectors.toList());
    }
}
