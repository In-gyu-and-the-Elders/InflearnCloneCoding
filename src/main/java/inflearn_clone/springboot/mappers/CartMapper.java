package inflearn_clone.springboot.mappers;

import inflearn_clone.springboot.domain.CartVO;
import inflearn_clone.springboot.dto.cart.CartOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    int regist(CartVO cartvo);
    List<CartOrderDTO> cartList(@Param("memberId") String memberId);
//    List<CartOrderDTO> goOrder(@Param("idxList") List<Integer> idxList, @Param("memberId") String memberId);
    int delete(@Param("idxList") List<Integer> idxList, @Param("memberId") String memberId);
    int cartCnt(@Param("courseIdx") int courseIdx, @Param("memberId") String memberId);
    int getCartCount(@Param("memberId") String memberId);
// 결제후 장바구니 삭제
    int deleteByCourseIdx(@Param("courseIdxList") List<Integer> courseIdxList, @Param("memberId") String memberId);
}


