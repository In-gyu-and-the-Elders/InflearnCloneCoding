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

    //@Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    // 스케쥴러 언제 실횅될지 정함
    public void processScheduler(){
        LocalDateTime now = LocalDateTime.now();
        boolean result = courseSerivce.updateCourseStatusToDeleted(now);
        if (result) {
            log.info("강좌 상태가 삭제로 변경되었습니다.");
        } else {
            log.info("삭제 상태로 변경할 강좌가 없습니다.");
        }

    }
}
