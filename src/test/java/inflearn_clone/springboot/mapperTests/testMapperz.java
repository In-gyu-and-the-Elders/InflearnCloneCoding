package inflearn_clone.springboot.mapperTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testMapperz {
    @Autowired
    private testMapper testMapper;


    @Test
    public void testMapper() {
        String now = testMapper.now();
    }



}
