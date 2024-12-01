package inflearn_clone.springboot.service.admin;

import inflearn_clone.springboot.domain.BbsVO;
import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.dto.course.CourseDTO;
import inflearn_clone.springboot.mappers.BbsMapper;
import inflearn_clone.springboot.mappers.CourseMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NoticeServiceImpl implements NoticeServiceIf{
    private final BbsMapper bbsMapper;
    private final ModelMapper modelMapper;
    private final CourseMapper courseMapper;

    @Override
    public int insert(BbsDTO bbsDTO) {
        BbsVO bbsVO = modelMapper.map(bbsDTO, BbsVO.class);
        return bbsMapper.insert(bbsVO);
    }

    @Override
    public int update(BbsDTO bbsDTO) {
        BbsVO bbsVO = modelMapper.map(bbsDTO, BbsVO.class);
        return bbsMapper.update(bbsVO);
    }

    @Override
    public int delete(int idx) {
        return bbsMapper.delete(idx);
    }

    @Override
    public List<BbsDTO> list(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("sortQuery", sortQuery);

        List<BbsVO> list = bbsMapper.list(map);
        List<BbsDTO> dtoList = list.stream().map(vo -> modelMapper.map(vo, BbsDTO.class)).toList();
        return dtoList;
    }

    @Override
    public BbsDTO view(int idx) {
        BbsVO bbsVO = bbsMapper.view(idx);
        return modelMapper.map(bbsVO, BbsDTO.class);
    }

    @Override
    public int autoInsert(String memberId, List<CourseDTO> list, String adminId) {
        BbsVO bbsVO = new BbsVO();
        bbsVO.setWriterId(adminId);
        bbsVO.setTitle(memberId + "강사님 강좌 폐지 안내");
        StringBuilder content = new StringBuilder();
        content.append("30일 이후 다음 강좌들은 폐지됩니다\n");
        content.append("폐지 강좌 목록\n");

        // 강좌 인덱스 조회
        List<Integer> idxList = list.stream()
            .map(CourseDTO::getIdx)
            .collect(Collectors.toList());

        //강좌명 조회
        List<String> titleList = list.stream()
                .map(CourseDTO::getTitle)
                .collect(Collectors.toList());

        for(Integer idx : idxList){
            for(String title : titleList){
                courseMapper.updateDeleteDate(idx, LocalDateTime.now().plusDays(30).with(LocalTime.MIDNIGHT));
                LocalDateTime month = LocalDateTime.now().plusDays(30);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = month.format(formatter);
                content.append(title);
                content.append(formattedDate);
                content.append("\n");
            }
        }
        bbsVO.setCategory("N");
        bbsVO.setContent(content.toString());
        int result = bbsMapper.insert(bbsVO);
        return result;
    }

    @Override
    public int autoInsertOneCourse(String memberId, CourseDTO info) {
        System.out.println("memberId: " + memberId);
        BbsVO bbsVO = new BbsVO();
        bbsVO.setWriterId(memberId);
        bbsVO.setTitle(info.getTeacherId() + "강사님 강좌 폐지 안내");
        StringBuilder content = new StringBuilder();
        content.append("30일 이후 해당 강좌는 폐지됩니다\n");
        courseMapper.updateDeleteDate(info.getIdx(), LocalDateTime.now().plusDays(30).with(LocalTime.MIDNIGHT));
        LocalDateTime month = LocalDateTime.now().plusDays(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = month.format(formatter);
        content.append(formattedDate);
        content.append(info.getTitle());
        bbsVO.setCategory("N");
        bbsVO.setContent(content.toString());
        int result = bbsMapper.insert(bbsVO);
        return result;
    }

    @Override
    public int noticeTotalCnt(String searchCategory, String searchValue) {
        return bbsMapper.noticeTotalCnt(searchCategory, searchValue);
    }
}
