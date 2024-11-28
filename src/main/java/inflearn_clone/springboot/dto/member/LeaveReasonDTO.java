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
public class LeaveReasonDTO {
    private String memberId;
    private String name;
    private LocalDateTime regDate;
    private String memberType;
    private String leaveReason;
    private LocalDateTime leaveReasonRegDate;

}
