package inflearn_clone.springboot.domain;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BbsVO {
    private int idx;
    private String category;
    private int refIdx;
    private String title;
    private String content;
    private LocalDateTime displayDate;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private int readCnt;
    private String filePath;
    private int courseIdx;
    private String writerId;
}
