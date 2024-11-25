package inflearn_clone.springboot.dto.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {
    private String memberId;
    private String pwd;
    private String name;
    private String phone;
    private String email;
    private String status;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private LocalDateTime leaveDate;
    private String memberType;
}
