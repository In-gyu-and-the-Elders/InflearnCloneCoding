package inflearn_clone.springboot.service.order;

import inflearn_clone.springboot.domain.OrderVO;
import inflearn_clone.springboot.dto.cart.CartOrderDTO;
import inflearn_clone.springboot.dto.member.MemberDTO;
import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
import inflearn_clone.springboot.dto.order.OrderRefundDTO;
import inflearn_clone.springboot.mappers.CartMapper;
import inflearn_clone.springboot.mappers.OrderMapper;
import inflearn_clone.springboot.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderServiceImpl implements OrderService  {
    private final ModelMapper modelMapper;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Override
    public void regist(OrderDTO orderDTO) {
        orderMapper.regist(orderDTO);
    }
    // 주문 리스트 조회
    @Override
    public List<OrderCourseDTO> getOrderList(String memberId) {
        return orderMapper.getOrderList(memberId);
    }

    @Override
    public boolean orderCnt(int courseIdx, String memberId) {
        int cnt = orderMapper.orderCnt(courseIdx, memberId);
        return cnt == 0;
    }

    @Override
    public List<OrderRefundDTO> refundByDeleteCourse(int idx) {
        System.out.println("idx" + idx);
        List<OrderVO> list = orderMapper.refundByDeleteCourse(idx);
        if(list == null){
            return null;
        }
        return list.stream()
            .map(vo -> modelMapper.map(vo, OrderRefundDTO.class)).collect(Collectors.toList());
    }
    @Override
    public boolean refundOrder(int idx) {
        return orderMapper.refundOrder(idx) == 0;
    }
    @Override
    public int studentCnt(int courseIdx) {
        return orderMapper.studentCnt(courseIdx);
    }


}
