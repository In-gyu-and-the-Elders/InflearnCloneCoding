package inflearn_clone.springboot.dto.section;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class SectionLessonFileDTO {
    private Integer sectionIdx;          // 섹션 ID
    private List<String> lessons;  // 강의 제목 리스트
    private List<MultipartFile> files ;  // 강의 파일 리스트
}
