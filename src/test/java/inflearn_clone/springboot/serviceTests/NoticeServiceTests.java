package inflearn_clone.springboot.serviceTests;

import inflearn_clone.springboot.dto.bbs.BbsDTO;
import inflearn_clone.springboot.service.NoticeServiceIf;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Log4j2
public class NoticeServiceTests {
    @Autowired
    private NoticeServiceIf noticeServiceIf;

    @Test
    public void insert(){
        BbsDTO bbsDTO = BbsDTO.builder()
                .idx(1)
                .title("테스트 타이틀")
                .content("test content")
                .displayDate(LocalDateTime.now())
                .filePath("filePath ex")
                .build();
        int result = noticeServiceIf.insert(bbsDTO);
        log.info(result);
    }

    @Test
    public void update(){
        BbsDTO bbsDTO = BbsDTO.builder()
                .idx(1)
                .title("수정타이틀")
                .content("수정내용")
                .displayDate(LocalDateTime.now())
                .filePath("수정파일경로")
                .build();
        int result = noticeServiceIf.update(bbsDTO);
        log.info(result);
    }

    @Test
    public void delete(){
        int result = noticeServiceIf.delete(1);
    }

    @Test
    public void list(){}

    @Test
    public void view(){
        BbsDTO bbsDTO = noticeServiceIf.view(2);
        log.info(bbsDTO);
    }
}
