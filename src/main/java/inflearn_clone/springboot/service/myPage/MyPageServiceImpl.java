package inflearn_clone.springboot.service.myPage;

import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.mypage.MyPageDTO;
import inflearn_clone.springboot.mappers.MyPageMapper;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final MyPageMapper myPageMapper;

    @Override
    public List<MyPageDTO> getMyCourses(String memberId) {
        return myPageMapper.getMyCourses(memberId);
    }
}