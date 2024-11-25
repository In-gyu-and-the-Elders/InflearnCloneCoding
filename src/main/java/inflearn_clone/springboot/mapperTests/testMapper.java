package inflearn_clone.springboot.mapperTests;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface testMapper {
    public String now();
}
