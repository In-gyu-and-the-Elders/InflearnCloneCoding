package inflearn_clone.springboot.service.section;

import inflearn_clone.springboot.domain.SectionVO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.mappers.SectionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class SectionServiceImpl implements SectionServiceIf {

    private final ModelMapper modelMapper;
    private final SectionMapper sectionMapper;

    @Override
    public int insertSection(SectionDTO sectionDTO) {
        SectionVO sectionVO = modelMapper.map(sectionDTO, SectionVO.class);
        return sectionMapper.insertSection(sectionVO);
    }

    @Override
    public List<SectionDTO> sectionList(int courseIdx) {
        List<SectionVO> sectionVOList = sectionMapper.sectionList(courseIdx);
        return sectionVOList.stream().map(vo -> modelMapper.map(vo, SectionDTO.class)).toList();
    }
}
