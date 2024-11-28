package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.SectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SectionMapper {
    int insertSection(SectionVO sectionVO);
    List<SectionVO> sectionList(@Param("courseIdx") int courseIdx);
}
