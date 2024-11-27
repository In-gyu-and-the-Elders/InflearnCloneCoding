package inflearn_clone.springboot.service.order;

import inflearn_clone.springboot.dto.cart.CartOrderDTO;
import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.mappers.CartMapper;
import inflearn_clone.springboot.mappers.OrderMapper;
import inflearn_clone.springboot.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Log4j2
@Service
public class OrderServiceImpl implements OrderService  {
    private final ModelMapper modelMapper;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final CartService cartService;



    @Override
    public boolean processOrder(String memberId) {
        // 1. 장바구니에서 상품 조회
        List<CartOrderDTO> cartList = cartMapper.cartList(memberId);

        // 2. 각 장바구니 아이템에 대해 주문 내역을 tbl_order에 삽입
        for (CartOrderDTO cartItem : cartList) {
            String orderNumber = generateOrderNumber();

            OrderCourseDTO order = new OrderCourseDTO();
            order.setMemberId(memberId);
            order.setCourseIdx(cartItem.getCourseIdx());
            order.setOrderPrice(cartItem.getPrice());
            order.setOrderNumber(orderNumber);
            orderMapper.regist(order);
        }

        return false;
    }

    // 결제번호 생성
    private String generateOrderNumber() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return date + UUID.randomUUID().toString();
    }

    // 주문 리스트 조회
    @Override
    public List<OrderCourseDTO> getOrderList(String memberId) {
        return orderMapper.getOrderList(memberId);
    }



}
