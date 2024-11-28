package inflearn_clone.springboot.service.admin;

import inflearn_clone.springboot.domain.AdminVO;
import inflearn_clone.springboot.dto.admin.AdminLoginDTO;
import inflearn_clone.springboot.mappers.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminServiceIf {
    
    private final AdminMapper adminMapper;
    
    @Override
    public boolean loginAdmin(AdminLoginDTO loginDTO) {
        AdminVO admin = adminMapper.selectAdminById(loginDTO.getAdminId());
        
        if(admin == null) {
            return false;
        }
        
        return loginDTO.getAdminPwd().equals(admin.getAdminPwd());
    }
} 