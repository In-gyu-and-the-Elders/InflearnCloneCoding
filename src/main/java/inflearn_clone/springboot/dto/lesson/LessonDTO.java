package inflearn_clone.springboot.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class LessonDTO {
    private int idx;
    private int sectionIdx;
    private String title;
    private String video;
    private LocalDateTime regDate;

    private MultipartFile videoFile;
}