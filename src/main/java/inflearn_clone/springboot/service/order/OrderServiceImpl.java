package inflearn_clone.springboot.service.order;

import inflearn_clone.springboot.domain.OrderVO;
import inflearn_clone.springboot.dto.cart.CartOrderDTO;
import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
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



    // 결제 처리 메서드
    /*
    @Override
    public boolean processOrder(String memberId, List<Integer> idxList) {
        List<CartOrderDTO> cartOrderList = cartService.cartList(memberId);

        List<CartOrderDTO> selectedProducts = cartOrderList.stream()
                .filter(cartOrder -> idxList.contains(cartOrder.getIdx()))
                .collect(Collectors.toList());

        String orderNumber = generateOrderNumber();

        for (CartOrderDTO cartOrder : selectedProducts) {
            boolean orderResult = placeOrder(cartOrder, memberId, orderNumber);
            if (!orderResult) {
                return false;
            }
        }

        int deleteResult = cartService.delete(idxList, memberId);
        return deleteResult > 0;
    }

    private String generateOrderNumber() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomPart = String.format("%05d", new Random().nextInt(100000));
        return date + randomPart; //
    }

    private boolean placeOrder(CartOrderDTO cartOrder, String memberId, String orderNumber) {
        CartOrderDTO cartOrder = new CartOrderDTO();
        cartOrder.setMemberId(memberId);
        cartOrder.setCourseIdx(cartOrder.getCourseIdx());
        cartOrder.setOrderPrice(cartOrder.getPrice());
        cartOrder.setOrderNumber(orderNumber);

        int result = orderMapper.regist(cartOrder);
        return result > 0;
    }
*/
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

}
