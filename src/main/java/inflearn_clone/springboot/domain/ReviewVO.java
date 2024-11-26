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
public class ReviewVO {
    private int idx;
    private int courseIdx;
    private String memberId;
    private String content;
    private int rating;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
}