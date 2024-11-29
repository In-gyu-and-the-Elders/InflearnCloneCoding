package inflearn_clone.springboot.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ReviewListDTO {
    private int idx;
    private int courseIdx;
    private String memberId;
    @NotBlank
    @Size(min = 5, max = 500)
    private String content;
    @Min(1)  // 최소값 1
    @Max(5)  // 최대값 5
    private int rating;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private String memberName;
}