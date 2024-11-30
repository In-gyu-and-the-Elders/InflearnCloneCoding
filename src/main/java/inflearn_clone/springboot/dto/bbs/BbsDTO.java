package inflearn_clone.springboot.dto.bbs;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class BbsDTO {
    private int idx;
    private String category;
    private int refIdx;
    @NotNull(message = "제목은 필수입니다.")
    @Size(min = 5, max = 50, message = "제목은 5자에서 50자 사이여야 합니다.")
    private String title;
    @NotNull(message = "내용은 필수입니다.")
    @Size(min = 10, max = 1000, message = "내용은 10자에서 1000자 사이여야 합니다.")
    private String content;

    @FutureOrPresent(message = "디스플레이 날짜는 현재 또는 미래여야 합니다.")
    private LocalDateTime displayDate;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private int readCnt;
    private String filePath;
    private int courseIdx;
    private String writerId;

    // 추가로 필요한 것들
    // [파일 업로드]
    private MultipartFile file;
    private String fileName;
    private String ext;


    public void setFileName(){
        if(this.filePath != null && !this.filePath.isEmpty()){
            Path path = Paths.get(filePath);
            this.fileName = path.getFileName().toString();
        }
    }
    public void setExt(){
        if(this.ext != null && !this.ext.isEmpty()){
            int lastDotIndex = fileName.lastIndexOf(".");
            if(lastDotIndex != -1){
                this.ext = fileName.substring(lastDotIndex+1);
            }
        }
    }
}
