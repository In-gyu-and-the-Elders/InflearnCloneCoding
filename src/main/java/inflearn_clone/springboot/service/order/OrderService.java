package inflearn_clone.springboot.service.order;

import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;

import java.util.List;

public interface OrderService {
    void regist(OrderDTO orderDTO);
    List<OrderCourseDTO> getOrderList(String memberId);
    boolean orderCnt(int courseIdx, String memberId);
}
