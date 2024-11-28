package inflearn_clone.springboot.service.teacher;

import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class TeacherServiceImpl {

    private final ModelMapper modelMapper;
    private final CourseMapper courseMapper;

}
