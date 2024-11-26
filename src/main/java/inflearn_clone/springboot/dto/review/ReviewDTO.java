package inflearn_clone.springboot.dto.review;

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
public class ReviewDTO {
    private int idx;
    private int courseIdx;
    private String memberId;
    private String content;
    private int rating;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
}