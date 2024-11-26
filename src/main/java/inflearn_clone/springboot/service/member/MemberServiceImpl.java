package inflearn_clone.springboot.service.member;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public int memberTotalCnt(String searchCategory, String searchValue) {
        log.info("searchCategory", searchCategory);
        log.info("searchValue", searchValue);
        return memberMapper.memberTotalCnt(searchCategory, searchValue);
    }

    @Override
    public List<MemberDTO> selectAllMember(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);

        List<MemberVO> voList =  memberMapper.selectAllMember(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class)).collect(Collectors.toList());
    }

    @Override
    public MemberDTO selectMemberInfo(String memberId) {
        MemberVO vo = memberMapper.selectMemberInfo(memberId);
        MemberDTO memberInfo = modelMapper.map(vo, MemberDTO.class);
        return memberInfo;
    }

    @Override
    public boolean modifyMemberInfo(MemberDTO dto) {
        System.out.println("Service Layer에서 받은 DTO: " + dto); // 디버그
        MemberVO vo = modelMapper.map(dto, MemberVO.class);
        System.out.println("Mapper로 전달될 VO: " + vo); // 디버그
        boolean result = memberMapper.modifyMemberInfo(vo);
        if(result){
            return result;
        }
        else{
            log.info("회원 수정 실패");
        }
        return false;
    }
}
