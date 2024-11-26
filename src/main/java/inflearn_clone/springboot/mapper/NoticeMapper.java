package inflearn_clone.springboot.mapper;

import inflearn_clone.springboot.domain.BbsVO;
import inflearn_clone.springboot.dto.bbs.BbsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    public int insert(BbsVO bbsVO);
    public int update(BbsVO bbsVO);
    public int delete(int idx);
    public List<BbsVO> list(); //페이징, 검색 등 추가 필요
    public BbsVO view(int idx);
}
