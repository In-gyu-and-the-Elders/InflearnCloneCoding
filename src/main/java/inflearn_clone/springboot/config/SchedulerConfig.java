package inflearn_clone.springboot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Log4j2
public class SchedulerConfig {
    @Scheduled(cron = "0 0 12 * * ?")
    public void run() {
        System.out.println("Cron 표현식 실행");
    }
}
