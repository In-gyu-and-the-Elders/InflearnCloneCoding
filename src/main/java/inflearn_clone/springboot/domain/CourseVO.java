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
public class CourseVO {
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
