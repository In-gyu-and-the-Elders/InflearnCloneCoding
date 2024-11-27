package inflearn_clone.springboot.service.course;


import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseSerivce {
    private final CourseMapper courseMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<CourseDTO> courseList() {
        List<CourseVO> voList = courseMapper.courseList();
        return voList.stream()
                .map(vo -> modelMapper.map(vo, CourseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO courseView(int idx) {
        CourseVO courseVO = courseMapper.courseView(idx);
        if (courseVO != null) {
            return modelMapper.map(courseVO, CourseDTO.class);
        }
        return null;
    }
}
