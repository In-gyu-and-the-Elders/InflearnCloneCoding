package inflearn_clone.springboot.service.admin;

import inflearn_clone.springboot.domain.BbsVO;
import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.mappers.BbsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NoticeServiceImpl implements NoticeServiceIf{
    private final BbsMapper bbsMapper;
    private final ModelMapper modelMapper;

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
    public List<BbsDTO> list() {
        List<BbsVO> list = bbsMapper.list();
        List<BbsDTO> dtoList = list.stream().map(vo -> modelMapper.map(vo, BbsDTO.class)).toList();
        return dtoList;
    }

    @Override
    public BbsDTO view(int idx) {
        BbsVO bbsVO = bbsMapper.view(idx);
        return modelMapper.map(bbsVO, BbsDTO.class);
    }
}
