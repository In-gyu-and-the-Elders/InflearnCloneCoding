package inflearn_clone.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonStatusVO {
    private int idx;
    private int lessonIdx;
    private String lessonFlag; // Lesson status flag (N: Not attended, Y: Attended)
    private String memberId;
}