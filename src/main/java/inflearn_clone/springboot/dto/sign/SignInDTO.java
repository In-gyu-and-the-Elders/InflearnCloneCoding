package inflearn_clone.springboot.dto.sign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInDTO {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String memberId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String pwd;
}