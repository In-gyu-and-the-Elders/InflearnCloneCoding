package inflearn_clone.springboot.service.myPage;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;

import java.util.List;

public interface MyPageService {
    List<MyPageDTO> getMyCourses(String memberId);
}
