package inflearn_clone.springboot.dto.cart;

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
public class CartOrderDTO {
    private int idx;
    private int courseIdx;
    private String memberId;
    private String teacherId;
    private int price;
    private LocalDateTime regDate;
    private String thumbnail;
    private List<Integer> idxList;
    private String title;
}
