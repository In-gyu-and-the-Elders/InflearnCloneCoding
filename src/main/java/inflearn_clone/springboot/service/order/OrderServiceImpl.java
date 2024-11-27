package inflearn_clone.springboot.service.order;

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
        // 1. 결제할 상품들 정보 가져오기 (cartService.cartList 사용)
        List<CartOrderDTO> cartOrderList = cartService.cartList(memberId);  // 장바구니 상품 목록 가져오기

        // 2. 결제할 상품들 필터링 (idxList에 포함된 상품만)
        List<CartOrderDTO> selectedProducts = cartOrderList.stream()
                .filter(cartOrder -> idxList.contains(cartOrder.getIdx()))
                .collect(Collectors.toList());

        // 3. 주문 번호 생성
        String orderNumber = generateOrderNumber();

        // 4. 선택한 상품들을 주문 테이블에 추가
        for (CartOrderDTO cartOrder : selectedProducts) {
            boolean orderResult = placeOrder(cartOrder, memberId, orderNumber);
            if (!orderResult) {
                return false;  // 주문 실패 시 처리
            }
        }

        // 5. 결제 후 장바구니에서 해당 상품들 삭제
        int deleteResult = cartService.delete(idxList, memberId);  // 장바구니 삭제
        return deleteResult > 0; // 삭제 성공 여부
    }

    // 주문 번호 생성 (날짜 + 랜덤 번호)
    private String generateOrderNumber() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomPart = String.format("%05d", new Random().nextInt(100000)); // 난수 5자리
        return date + randomPart; // 예: 20231127001234
    }

    // 주문 내역을 tbl_order에 삽입
    private boolean placeOrder(CartOrderDTO cartOrder, String memberId, String orderNumber) {
        CartOrderDTO cartOrder = new CartOrderDTO();
        cartOrder.setMemberId(memberId);
        cartOrder.setCourseIdx(cartOrder.getCourseIdx());
        cartOrder.setOrderPrice(cartOrder.getPrice());  // 장바구니에서 가져온 가격
        cartOrder.setOrderNumber(orderNumber);

        // 주문 테이블에 저장
        int result = orderMapper.regist(cartOrder);
        return result > 0;  // 삽입 성공 여부
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



}
