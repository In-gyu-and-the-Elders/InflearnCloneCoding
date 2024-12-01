package inflearn_clone.springboot.service.member;

import inflearn_clone.springboot.dto.member.LeaveReasonDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MemberServiceIf {
    // 전체 회원 수
    int memberTotalCnt(String searchCategory, String searchValue, String memberType, String status);

    // 탈퇴 요청 선생님 회원 수
    int teacherRequestTotalCnt(String searchCategory, String searchValue, String memberType);

    int memberStatusTotalCnt(String status, String memberType, LocalDateTime startDate, LocalDateTime endDate);

    //전체 회원 조회
    List<MemberDTO> selectAllMember(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery, String memberType, String status);

    //선생님 조회(탈퇴 요청 상태일 때)
    List<MemberDTO> selectTeacherRequest(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

    //아이디로 회원 조회
    MemberDTO selectMemberInfo(String memberId);

    //회원 수정
    boolean modifyMemberInfo(MemberDTO dto);

    //회원 탈퇴 이유 상세
    LeaveReasonDTO leaveReasonView(String memberId);

    // 회원 삭제
    boolean deleteMemberInfo(String memberId);

    //엑셀 내보내기
    List<MemberDTO> selectMemberInfoByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 회원 상태 수정
    boolean changeMemberStatus(String memberId);
}
