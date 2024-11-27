package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.OrderVO;
import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    int regist(OrderCourseDTO orderCourseDTO);
    List<OrderCourseDTO> getOrderList(@Param("memberId") String memberId);
}
