package inflearn_clone.springboot.service;


import inflearn_clone.springboot.domain.CourseVO;
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
    @Autowired
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

    @Override
    public int insertCourse(CourseDTO courseDTO) {
        CourseVO courseVO = modelMapper.map(courseDTO, CourseVO.class);
        return courseMapper.insertCourse(courseVO);
    }

    @Override
    public CourseDTO viewMyLastCourse(String memberId) {
        return modelMapper.map(courseMapper.viewMyLastCourse(memberId), CourseDTO.class);
    }


}
