package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.AdminVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    AdminVO selectAdminById(String adminId);
}
