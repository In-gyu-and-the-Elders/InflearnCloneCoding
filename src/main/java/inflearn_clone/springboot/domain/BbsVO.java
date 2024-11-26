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
