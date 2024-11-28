package inflearn_clone.springboot.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class OrderCourseDTO {
    private int idx;
    private String memberId;
    private int courseIdx;
    private int orderPrice;
    private LocalDateTime orderDate;
    private String orderNumber;
    private String title;

    private List<Integer> idxList;
    private List<Integer> priceList;
}
