package inflearn_clone.springboot.dto.member;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@ToString
public class MemberDTO {
    private String memberId;
    private String pwd;
    private String name;
    private String phone;
    private String email;
    private String status; // Status (Y: Active, N: Inactive)
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private LocalDateTime leaveDate;
    private String memberType; // Member type (S: Student, T: Teacher)
    private String teacherDesc;
}