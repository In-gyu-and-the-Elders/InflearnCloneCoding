package inflearn_clone.springboot.dto.main;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@ToString
public class MainDTO {
    private int idx;
    private String category;
    private String title;
    private String description;
    private String teacherId;
    private LocalDateTime regDate;
    private LocalDateTime displayDate;
    private LocalDateTime modifyDate;
    private LocalDateTime deleteDate;
    private String status;
    private int price;
    private String thumbnail;
    
    public String getCategoryName() {
        return switch (this.category) {
            case "D" -> "개발";
            case "A" -> "인공지능";
            case "H" -> "하드웨어";
            case "C" -> "커리어/자기계발";
            case "M" -> "기획/경영/마케팅";
            default -> "";
        };
    }
}