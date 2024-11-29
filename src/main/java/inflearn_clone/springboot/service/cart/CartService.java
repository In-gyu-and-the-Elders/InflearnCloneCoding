package inflearn_clone.springboot.service.cart;

import inflearn_clone.springboot.dto.cart.CartDTO;
import inflearn_clone.springboot.dto.cart.CartOrderDTO;

import java.util.List;

public interface CartService {
    boolean regist(CartOrderDTO cartOrderDTO);
    boolean cartCnt(int courseIdx,String memberId);
    List<CartOrderDTO> cartList(String memberId);
//    List<CartOrderDTO> goOrder(List<Integer> idxList, String memberId);
    int delete(List<Integer> idxList, String memberId);
    int getCartCount(String memberId);
}
