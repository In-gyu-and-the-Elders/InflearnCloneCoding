package inflearn_clone.springboot.dto.lessonStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class LessonStatusDTO {
    private int idx;
    private int lessonIdx;
    private String lessonFlag; // Lesson status flag (N: Not attended, Y: Attended)
    private String memberId;
}