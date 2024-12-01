package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.BbsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BbsMapper {
    public int insert(BbsVO bbsVO);
    public int update(BbsVO bbsVO);
    public int delete(int idx);
    public List<BbsVO> list(Map<String, Object> map);
    public BbsVO view(int idx);
    public List<BbsVO> listS(Map<String, Object> map);

    public int noticeTotalCnt(String searchCategory, String searchValue);
    public int noticeTotalCntS(String searchCategory, String searchValue);
}