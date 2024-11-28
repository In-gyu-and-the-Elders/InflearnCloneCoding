package inflearn_clone.springboot.dto.section;

import lombok.Data;

import java.util.List;
@Data
public class SectionWrapperDTO {
    private List<SectionLessonFileDTO> sections;
}
