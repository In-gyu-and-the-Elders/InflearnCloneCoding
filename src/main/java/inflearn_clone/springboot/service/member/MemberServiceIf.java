package inflearn_clone.springboot.service.member;

import inflearn_clone.springboot.dto.member.MemberDTO;

import java.util.List;
import java.util.Map;

public interface MemberServiceIf {
    // 전체 회원 수
    int memberTotalCnt(String searchCategory, String searchValue);

    //전체 회원 조회
    List<MemberDTO> selectAllMember(int pageNo, int pageSize, String searchCategory, String searchValue);
}
