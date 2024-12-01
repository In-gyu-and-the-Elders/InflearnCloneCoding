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
public class OrderVO {
    private int idx;
    private String memberId;
    private int courseIdx;
    private int orderPrice;
    private LocalDateTime orderDate;
    private String orderNumber;
    private LocalDateTime leaveDate;
}
