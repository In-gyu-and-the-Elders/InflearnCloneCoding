package inflearn_clone.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Data
public class CourseVO {
    private int idx;
    private String category;
    private String title;
    private String description;
    private String teacherId;
    private String regDate;
    private String displayDate;
    private String modifyDate;
    private String deleteDate;
    private String status;
    private int price;
    private String thumbnail;
}
