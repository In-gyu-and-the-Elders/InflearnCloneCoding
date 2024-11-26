package inflearn_clone.springboot.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class OrderDTO {
    private int idx;
    private String memberId;
    private int courseIdx;
    private int orderPrice;
    private LocalDateTime orderDate;
    private String orderNumber;
}
