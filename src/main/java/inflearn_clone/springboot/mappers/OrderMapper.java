package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    int regist(OrderDTO order);
    List<OrderCourseDTO> getOrderList(@Param("memberId") String memberId);
}
