package inflearn_clone.springboot.service.order;

import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
import inflearn_clone.springboot.dto.order.OrderRefundDTO;

import java.util.List;

public interface OrderService {
    void regist(OrderDTO orderDTO);
    List<OrderCourseDTO> getOrderList(String memberId);
    boolean orderCnt(int courseIdx, String memberId);
    List<OrderRefundDTO> refundByDeleteCourse(int idx);
    boolean refundOrder(int courseIdx, String memberId);
    int studentCnt(int courseIdx);
}
