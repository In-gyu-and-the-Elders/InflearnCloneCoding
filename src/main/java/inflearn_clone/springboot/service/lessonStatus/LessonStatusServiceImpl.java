package inflearn_clone.springboot.service.lessonStatus;

import inflearn_clone.springboot.mappers.LessonStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class LessonStatusServiceImpl implements LessonStatusService {
    private final LessonStatusMapper lessonStatusMapper;

    @Override
    public void updateLessonStatus(int lessonIdx, String memberId) {
        int count = lessonStatusMapper.countLessonStatus(lessonIdx, memberId);
        if (count > 0) {
            lessonStatusMapper.updateLessonStatus(lessonIdx, memberId);
        } else {
            lessonStatusMapper.insertLessonStatus(lessonIdx, memberId);
        }
    }
}
