package inflearn_clone.springboot.dto.mypage;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@ToString
public class MyPageDTO {
  private int idx;
  private int courseIdx;
  private String title;
  private String content;
  private String courseTitle;
  private String description;
  private String category;
  private String memberId;
  private int price;
  private String thumbnail;
  private String teacherId;
  private String teacherName;
  private LocalDateTime orderDate;
  private int orderPrice;
  private int totalLessonCount;
  private LocalDateTime regDate;
  private LocalDateTime displayDate;
  private LocalDateTime modifyDate;
  private LocalDateTime deleteDate;
  private String status;
  private int studentCount;
  private int rating;
  private double avgRating;

  // 탈퇴
  private LocalDateTime leaveDate;
  private String leaveReason;
  private LocalDateTime leaveReasonRegDate;
}
