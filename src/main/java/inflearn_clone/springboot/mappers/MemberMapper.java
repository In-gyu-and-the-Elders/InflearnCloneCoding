package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.member.LeaveReasonDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    List<MemberVO> selectAllMember(Map<String, Object> map);
    int memberTotalCnt(@Param("searchCategory") String searchCategory, @Param("searchValue") String searchValue, @Param("memberType") String memberType, @Param("status") String status);

    int teacherRequestTotalCnt(@Param("searchCategory") String searchCategory, @Param("searchValue") String searchValue, @Param("memberType") String memberType);

    int memberStatusTotalCnt(@Param("status") String status, @Param("memberType") String memberType,
                             @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    MemberVO selectMemberInfo(String memberId);

    List<MemberVO> selectTeacherRequest(Map<String, Object> map);

    boolean modifyMemberInfo(MemberVO vo);

    MemberVO leaveReasonView(String memberId);

    boolean deleteMemberInfo(String memberId);
}
