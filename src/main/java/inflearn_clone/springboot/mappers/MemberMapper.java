package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    List<MemberVO> selectAllMember(Map<String, Object> map);
    int memberTotalCnt(@Param("searchCategory") String searchCategory, @Param("searchValue") String searchValue);
}
