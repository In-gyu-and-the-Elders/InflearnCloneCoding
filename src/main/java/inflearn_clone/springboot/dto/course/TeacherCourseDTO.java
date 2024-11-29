package inflearn_clone.springboot.dto.course;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@ToString
public class TeacherCourseDTO {
    private int idx;
    private String thumbnail;
    private String title;
    private String category;
    private String teacherId;

    private String status; // (Y: Active, N: Inactive, D: Deleted)
    private int sectionCnt;
    private int lessonCnt;
    private int price;

    private LocalDateTime regDate;
    private LocalDateTime displayDate;
}
