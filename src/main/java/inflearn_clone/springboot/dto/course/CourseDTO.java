package inflearn_clone.springboot.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class CourseDTO {
    private int idx;
    private String category;
    private String title;
    private String description;
    private String teacherId;
    private LocalDateTime regDate;
    private LocalDateTime displayDate;
    private LocalDateTime modifyDate;
    private LocalDateTime deleteDate;
    private String status; // (Y: Active, N: Inactive, D: Deleted)
    private int price;
    private String thumbnail; // Thumbnail image path
}
