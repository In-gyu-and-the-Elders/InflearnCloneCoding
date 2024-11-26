package inflearn_clone.springboot.dto.section;

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
public class SectionDTO {
    private int idx;
    private int courseIdx;
    private String section;
    private LocalDateTime regDate;
}
