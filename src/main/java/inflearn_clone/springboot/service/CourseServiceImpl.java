package inflearn_clone.springboot.service;


import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public CourseDTO courseView(int idx) {
        return null;
    }

    //teacherId로 존재하는 강의가 있는지 확인하기
    @Override
    public List<Integer> selectCourseByMemberId(String memberId) {

        return courseMapper.selectCourseByMemberId(memberId);
    }

    // 특정 teacherId의 강의 삭제하기
    @Override
    public boolean deleteCourseByMemberId(String memberId) {
        return courseMapper.deleteCourseByMemberId(memberId);
    }

}
