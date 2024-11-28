package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.dto.mypage.MyPageDTO;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageMapper {
  List<MyPageDTO> getMyCourses(@Param("memberId") String memberId);
}
