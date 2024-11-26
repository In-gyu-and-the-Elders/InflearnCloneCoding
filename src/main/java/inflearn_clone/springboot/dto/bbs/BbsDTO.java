package inflearn_clone.springboot.dto.bbs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class BbsDTO {
    private int idx;
    private int level;
    private int refIdx;
    private String title;
    private String content;
    private LocalDateTime displayDate;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private int readCnt;
    private String filePath;
    private int courseIdx;

    // 추가로 필요한 것들
    // [파일 업로드]
    private MultipartFile file;
}
