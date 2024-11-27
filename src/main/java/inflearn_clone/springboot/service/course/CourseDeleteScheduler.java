package inflearn_clone.springboot.service.course;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseDeleteScheduler {
    private final CourseSerivce courseSerivce;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행 ?
    // 스케쥴러 언제 실횅될지 정함
    public void processScheduler(){
        LocalDateTime now = LocalDateTime.now();
        boolean result = courseSerivce.updateCourseStatusToDeleted(now);

    }
}
