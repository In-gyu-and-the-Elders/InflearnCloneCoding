package inflearn_clone.springboot.service.order;

import inflearn_clone.springboot.dto.order.OrderCourseDTO;

import java.util.List;

public interface OrderService {
    boolean processOrder(String memberId);
    List<OrderCourseDTO> getOrderList(String memberId);
}
