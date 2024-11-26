package inflearn_clone.springboot.service.admin;

import inflearn_clone.springboot.dto.bbs.BbsDTO;

import java.util.List;

public interface NoticeServiceIf {
    public int insert(BbsDTO bbsDTO);
    public int update(BbsDTO bbsDTO);
    public int delete(int idx);
    public List<BbsDTO> list(); //페이징, 검색 등 추가 필요
    public BbsDTO view(int idx);
}
