package inflearn_clone.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonVO {
    private int idx;
    private int sectionIdx;
    private String title;
    private String video;
    private LocalDateTime regDate;
}