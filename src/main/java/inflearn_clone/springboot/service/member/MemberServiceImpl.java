package inflearn_clone.springboot.service.member;

import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        return memberMapper.memberTotalCnt(searchCategory, searchValue);
    }

    @Override
    public List<MemberDTO> selectAllMember(int pageNo, int pageSize, String searchCategory, String searchValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);

        List<MemberVO> voList =  memberMapper.selectAllMember(map);
        return voList.stream()
                .map(vo -> modelMapper.map(vo, MemberDTO.class)).collect(Collectors.toList());
    }
}
