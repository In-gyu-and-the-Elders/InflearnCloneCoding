package inflearn_clone.springboot.service.course;


import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import inflearn_clone.springboot.utils.CategoryMapper;
import inflearn_clone.springboot.utils.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<CourseDTO> getCourses(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        if ("category".equals(searchCategory)) {
            searchValue = CategoryMapper.getCode(searchValue);
        }
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);

        List<CourseVO> voList = courseMapper.selectAllCourse(map);
        return voList.stream().map(vo -> modelMapper.map(vo, CourseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public int getTotalCourses(String searchCategory, String searchValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("searchCategory", searchCategory);

        // 카테고리 검색일 경우 코드로 변환
        if ("category".equals(searchCategory)) {
            searchValue = CategoryMapper.getCode(searchValue);
        }
        map.put("searchValue", searchValue);
        return courseMapper.courseTotalCnt(searchCategory, searchValue);
    }

}
