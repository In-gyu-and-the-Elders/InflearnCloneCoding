package inflearn_clone.springboot.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class LikeDTO {
    private int idx;
    private int courseIdx;
    private String membetId;
}
