package inflearn_clone.springboot.service.cart;

import inflearn_clone.springboot.domain.CartVO;
import inflearn_clone.springboot.dto.cart.CartDTO;
import inflearn_clone.springboot.dto.cart.CartOrderDTO;
import inflearn_clone.springboot.mappers.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;
    private final ModelMapper modelMapper;

    @Override
    public boolean regist(CartOrderDTO cartOrderDTO) {
        CartVO cartVO = modelMapper.map(cartOrderDTO, CartVO.class);
        int result = cartMapper.regist(cartVO);
        return result > 0;
    }

    @Override
    public boolean cartCnt(int courseIdx, String memberId) {
        int count = cartMapper.cartCnt(courseIdx, memberId);
        return count == 0;
    }

    @Override
    public List<CartOrderDTO> cartList(String memberId) {
        return cartMapper.cartList(memberId);
    }

//    @Override
//    public List<CartOrderDTO> goOrder(List<Integer> idxList, String memberId) {
//        return cartMapper.goOrder(idxList, memberId);
//    }

    @Override
    public int delete(List<Integer> idxList, String memberId) {
        return cartMapper.delete(idxList, memberId);
    }

    @Override
    public int getCartCount(String memberId) {
        return cartMapper.getCartCount(memberId);
    }

    //결제후 장바구니 삭제
    @Override
    public int deleteByCourseIdx(List<Integer> courseIdxList, String memberId) {
        return cartMapper.deleteByCourseIdx(courseIdxList, memberId);
    }
}
