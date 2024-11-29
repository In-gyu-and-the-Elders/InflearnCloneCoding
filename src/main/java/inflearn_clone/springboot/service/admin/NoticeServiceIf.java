package inflearn_clone.springboot.service.admin;

import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.dto.course.CourseDTO;

import java.util.List;

public interface NoticeServiceIf {
    public int insert(BbsDTO bbsDTO);
    public int update(BbsDTO bbsDTO);
    public int delete(int idx);
    public List<BbsDTO> list(int pageNo, int pageSize, String searchCategory, String searchValue, String sortQuery);

    BbsDTO view(int idx);

    //수미가 작성한 것
    public int autoInsert(String memberId, List<CourseDTO> list);

    int noticeTotalCnt(String searchCategory, String searchValue);


}
