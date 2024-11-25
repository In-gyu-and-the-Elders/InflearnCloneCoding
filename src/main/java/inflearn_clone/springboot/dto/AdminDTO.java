package inflearn_clone.springboot.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AdminDTO {
    private String adminId;
    private String adminPwd;
}
