package inflearn_clone.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
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
    private String leaveReason;
    private LocalDateTime leaveReasonRegDate;
}