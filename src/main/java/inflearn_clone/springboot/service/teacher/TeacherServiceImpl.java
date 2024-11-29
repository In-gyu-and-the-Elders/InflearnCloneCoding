package inflearn_clone.springboot.service.teacher;

import inflearn_clone.springboot.domain.CourseVO;
import inflearn_clone.springboot.domain.MemberVO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.mappers.CourseMapper;
import inflearn_clone.springboot.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TeacherServiceImpl implements TeacherServiceIf{

    private final ModelMapper modelMapper;
    private final CourseMapper courseMapper;
    private final MemberMapper memberMapper;

    @Override
    public int teacherTotalCnt(String searchCategory, String searchValue, String memberType, String status) {
        return memberMapper.memberTotalCnt(searchCategory, searchValue, memberType, status);
    }

    @Override
    public List<MemberDTO> selectAllTeacher(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery, String memberType, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo -1) * pageSize);
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
}
