package inflearn_clone.springboot.dto.admin;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginDTO {

    private String adminId;

    private String adminPwd;
    
    private boolean rememberMe;
} 