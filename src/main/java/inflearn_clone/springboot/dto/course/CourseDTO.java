package inflearn_clone.springboot.dto.course;

import inflearn_clone.springboot.dto.section.SectionDTO;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@ToString
public class CourseDTO {
    private int idx;
    private int courseIdx;
    private int lessonIdx;
    private String category;
    private String title;
    private String courseTitle;
    private String lessonTitle;
    private String description;
    private String teacherId;
    private LocalDateTime regDate;
    private String section;
    private LocalDateTime displayDate;
    private LocalDateTime modifyDate;
    private LocalDateTime deleteDate;
    private String status; // (Y: Active, N: Inactive, D: Deleted)
    private int price;
    private String thumbnail; // Thumbnail image path

    private MultipartFile thumbnailFile;

    private List<SectionDTO> sections = new ArrayList<>();

}
