package inflearn_clone.springboot.dto.course;

import groovy.transform.builder.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@AllArgsConstructor
@NotNull
@Builder
public class CourseDTO {
    private int idx;
    private String category;
    @NotNull
    @Size(min = 5, max = 50)
    private String title;
    @NotNull
    @Size(min = 10, max = 1000)
    private String description;
    @NotNull
    private String teacherId;
    private String regDate;
    private String displayDate;
    private String modifyDate;
    private String deleteDate;
    private String status;
    private int price;
    private String thumbnail;
}
