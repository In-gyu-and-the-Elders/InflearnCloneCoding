package inflearn_clone.springboot.service.section;

import inflearn_clone.springboot.dto.section.SectionDTO;

import java.util.List;

public interface SectionServiceIf {
    int insertSection(SectionDTO sectionDTO);
    List<SectionDTO> sectionList(int courseIdx);

}
