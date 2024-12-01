package inflearn_clone.springboot.service.member;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.member.LeaveReasonDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Log4j2
@Service
public class MemberServiceImpl implements MemberServiceIf{

    private final MemberMapper memberMapper;

    private final ModelMapper modelMapper;

    @Override
    public int memberTotalCnt(String searchCategory, String searchValue, String memberType, String status) {
        log.info("searchCategory", searchCategory);
        log.info("searchValue", searchValue);
        log.info("memberType", memberType);
        log.info("status", status);
        return memberMapper.memberTotalCnt(searchCategory, searchValue, memberType, status);
    }

    @Override
    public int teacherRequestTotalCnt(String searchCategory, String searchValue, String memberType) {
        log.info("searchCategory", searchCategory);
        log.info("searchValue", searchValue);
        log.info("memberType", memberType);
        return memberMapper.teacherRequestTotalCnt(searchCategory, searchValue, memberType);
    }

    @Override
    public int memberStatusTotalCnt(String status, String memberType, LocalDateTime startDate, LocalDateTime endDate){
        log.info("stauts" +status);
        log.info("memberType" +memberType);
        log.info("startDate" +startDate);
        log.info("endDate" +endDate);
        return memberMapper.memberStatusTotalCnt(status, memberType, startDate, endDate);
    }

    @Override
    public List<MemberDTO> selectAllMember(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery, String memberType, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);
        map.put("memberType", memberType);
        map.put("status", status);

        List<MemberVO> voList =  memberMapper.selectAllMember(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class)).collect(Collectors.toList());
    }


    @Override
    public MemberDTO selectMemberInfo(String memberId) {
        MemberVO vo = memberMapper.selectMemberInfo(memberId);
        if (vo == null) {
            return null;
        }
        MemberDTO memberInfo = modelMapper.map(vo, MemberDTO.class);
        return memberInfo;
    }


    @Override
    public boolean modifyMemberInfo(MemberDTO dto) {
        MemberVO vo = modelMapper.map(dto, MemberVO.class);
        boolean result = memberMapper.modifyMemberInfo(vo);
        if(result){
            return result;
        }
        else{
            log.info("회원 수정 실패");
        }
        return false;
    }

    @Override
    public LeaveReasonDTO leaveReasonView(String memberId) {
        MemberVO vo = memberMapper.leaveReasonView(memberId);
        if(vo == null){
            return null;
        }
        return modelMapper.map(vo, LeaveReasonDTO.class);
    }

    @Override
    public boolean deleteMemberInfo(String memberId) {
        return memberMapper.deleteMemberInfo(memberId);
    }


    @Override
    public List<MemberDTO> selectTeacherRequest(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);

        List<MemberVO> voList =  memberMapper.selectTeacherRequest(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<MemberDTO> selectMemberInfoByDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // LocalDateTime -> String 변환
        String startDate = startDateTime.format(formatter);
        String endDate = endDateTime.format(formatter);

        List<MemberVO> voList = memberMapper.selectMemberInfoByDate(startDate, endDate);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean changeMemberStatus(String memberId) {
        return memberMapper.changeMemberStatus(memberId);
    }

}
