package inflearn_clone.springboot.dto.sign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@ToString
public class SignDTO {

  @NotBlank(message = "아이디는 필수 입력 값입니다.")
  @Pattern(regexp = "^[a-z0-9]{5,15}$", message = "아이디는 5~15자의 소문자와 숫자로만 구성되어야 합니다.")
  private String memberId;

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,20}$",
      message = "비밀번호는 10~20자의 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
  )
  private String pwd;

  @NotBlank(message = "이름은 필수 입력 값입니다.")
  @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 2~5자의 한글만 입력 가능합니다.")
  private String name;

  @NotBlank(message = "전화번호는 필수 입력 값입니다.")
  @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
  private String phone;

  @NotBlank(message = "이메일 형식이 올바르지 않습니다.")
  @Size(max = 254, message = "이메일은 최대 254자까지 입력 가능합니다.")
  @Pattern(regexp = "^(?=.{1,254}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
  private String email;
  private String status; // 상태 (Y: 활성, N: 비활성)
  private LocalDateTime regDate;
  private LocalDateTime modifyDate;
  private LocalDateTime leaveDate;
  private String memberType; // 회원 유형 (S: 학생, T: 교사)
}