package inflearn_clone.springboot.service.admin;


import inflearn_clone.springboot.dto.admin.AdminLoginDTO;

public interface AdminServiceIf {
    boolean loginAdmin(AdminLoginDTO loginDTO);
}
