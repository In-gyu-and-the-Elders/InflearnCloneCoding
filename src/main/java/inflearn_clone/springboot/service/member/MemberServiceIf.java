package inflearn_clone.springboot.service.member;

import inflearn_clone.springboot.dto.member.LeaveReasonDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;

import java.util.List;
import java.util.Map;

public interface MemberServiceIf {
    // 전체 회원 수
    int memberTotalCnt(String searchCategory, String searchValue);

    // 탈퇴 요청 선생님 회원 수
    int teacherRequestTotalCnt(String searchCategory, String searchValue, String memberType);

    //전체 회원 조회
    List<MemberDTO> selectAllMember(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

    //선생님 조회(탈퇴 요청 상태일 때)
    List<MemberDTO> selectTeacherRequest(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

    //아이디로 회원 조회
    MemberDTO selectMemberInfo(String memberId);

    //회원 수정
    boolean modifyMemberInfo(MemberDTO dto);

    //회원 탈퇴 이유 상세
    LeaveReasonDTO leaveReasonView(String memberId);

    //회원 삭제
    boolean deleteMemberInfo(String memberId);

}
