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
public class CourseTotalDTO {
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

    private MultipartFile thumbnailFile;
    private int studentCount; // 학생수

    private String teacherName;
    private Double averageRating;

}
