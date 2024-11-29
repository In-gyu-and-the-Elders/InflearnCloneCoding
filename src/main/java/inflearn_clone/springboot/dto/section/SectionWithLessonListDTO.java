package inflearn_clone.springboot.dto.section;

import inflearn_clone.springboot.dto.lesson.LessonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class SectionWithLessonListDTO {
    private int idx;
    private int courseIdx;
    private String section;
    private LocalDateTime regDate;

    private List<LessonDTO> lessons;
    private int lessonCount;
}
