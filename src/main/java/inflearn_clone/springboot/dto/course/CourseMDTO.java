package inflearn_clone.springboot.dto.course;

import inflearn_clone.springboot.dto.section.SectionDTO;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
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
public class CourseMDTO {
    private int idx;
    private int courseIdx;
    private int lessonIdx;
    @NotNull(message = "카테고리는 필수 항목입니다.")
    @Pattern(regexp = "^(D|A|H|C|M)$", message = "카테고리는 개발, 인공지능, 하드웨어, 커리어/자기계발, 기획/경영/마케팅 중 하나여야 합니다.")
    private String category;
    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(min = 5, max = 100, message = "제목은 최소 5자, 최대 100자여야 합니다.")
    private String title;

    private String courseTitle;
    private String lessonTitle;
    @NotBlank(message = "강의 소개는 필수 항목입니다.")
    @Size(min = 100, max = 2000, message = "강의 소개는 최소 100자, 최대 2000자여야 합니다.")
    private String description;
    private String teacherId;
    private LocalDateTime regDate;
    private String section;
    @NotNull(message = "강좌 노출일은 필수 항목입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "강좌 노출일은 미래여야 합니다.")
    private LocalDateTime displayDate;
    private LocalDateTime modifyDate;
    private LocalDateTime deleteDate;
    private String status; // (Y: Active, N: Inactive, D: Deleted)
    @NotNull(message = "가격은 필수 항목입니다.")
    @Positive(message = "가격은 양수여야 합니다.")
    private Integer price;
    private String thumbnail; // Thumbnail image path
    private MultipartFile thumbnailFile;

    private List<SectionDTO> sections = new ArrayList<>();
}
