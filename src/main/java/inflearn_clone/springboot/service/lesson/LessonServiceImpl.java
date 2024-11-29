package inflearn_clone.springboot.service.lesson;

import inflearn_clone.springboot.domain.LessonVO;
import inflearn_clone.springboot.domain.SectionVO;
import inflearn_clone.springboot.dto.lesson.LessonDTO;
import inflearn_clone.springboot.dto.section.SectionDTO;
import inflearn_clone.springboot.mappers.LessonMapper;
import inflearn_clone.springboot.mappers.SectionMapper;
import inflearn_clone.springboot.service.section.SectionServiceIf;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class LessonServiceImpl implements LessonServiceIf {

    private final ModelMapper modelMapper;
    private final LessonMapper lessonMapper;

    @Override
    public int insertLesson(LessonDTO lessonDTO) {
        LessonVO lessonVO = modelMapper.map(lessonDTO, LessonVO.class);
        return lessonMapper.insertLesson(lessonVO);
    }

    @Override
    public List<LessonDTO> getLessons(int sectionIdx) {
        List<LessonVO> lessonVOList = lessonMapper.getLessons(sectionIdx);
        return lessonVOList.stream().map(vo -> modelMapper.map(vo, LessonDTO.class)).toList();
    }

    @Override
    public int updateLesson(LessonDTO lessonDTO) {
        LessonVO lessonVO = modelMapper.map(lessonDTO, LessonVO.class);
        return lessonMapper.updateLesson(lessonVO);
    }

}
