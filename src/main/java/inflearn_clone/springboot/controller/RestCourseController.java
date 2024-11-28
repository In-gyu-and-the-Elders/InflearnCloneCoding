package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.service.CourseSerivce;
import inflearn_clone.springboot.utils.CommonFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class RestCourseController {

    private final CourseSerivce courseService;

    @PostMapping("/insertCourse")
    public Map<String, Object> insertCourse(@ModelAttribute CourseDTO courseDTO) throws IOException {
            log.info("Controller Method Start");
            String filePath = CommonFileUtil.uploadFile(courseDTO.getThumbnailFile());
            courseDTO.setThumbnail(filePath);
            courseDTO.setTeacherId("임시선생님1");
            log.info(courseDTO);
            int result = courseService.insertCourse(courseDTO);
            log.info(result);
            // 성공 응답 생성
            Map<String, Object> response = new HashMap<>();
            response.put("courseDTO", courseDTO);  // 강좌 등록 후 강좌정보 반환
            return response;
    }

}
