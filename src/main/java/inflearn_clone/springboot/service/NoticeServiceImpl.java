package inflearn_clone.springboot.service;

import inflearn_clone.springboot.domain.BbsVO;
import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.mappers.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NoticeServiceImpl implements NoticeServiceIf{
    private final NoticeMapper noticeMapper;
    private final ModelMapper modelMapper;

    @Override
    public int insert(BbsDTO bbsDTO) {
        BbsVO bbsVO = modelMapper.map(bbsDTO, BbsVO.class);
        return noticeMapper.insert(bbsVO);
    }

    @Override
    public int update(BbsDTO bbsDTO) {
        BbsVO bbsVO = modelMapper.map(bbsDTO, BbsVO.class);
        return noticeMapper.update(bbsVO);
    }

    @Override
    public int delete(int idx) {
        return noticeMapper.delete(idx);
    }

    @Override
    public List<BbsDTO> list() {
        List<BbsVO> list = noticeMapper.list();
        List<BbsDTO> dtoList = list.stream().map(vo -> modelMapper.map(vo, BbsDTO.class)).toList();
        return dtoList;
    }

    @Override
    public BbsDTO view(int idx) {
        BbsVO bbsVO = noticeMapper.view(idx);
        return modelMapper.map(bbsVO, BbsDTO.class);
    }
}
