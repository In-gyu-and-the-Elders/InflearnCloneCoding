package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.cart.CartOrderDTO;
import inflearn_clone.springboot.service.cart.CartService;
import inflearn_clone.springboot.utils.JSFunc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Log4j2
public class CartController {
    private final CartService cartService;

    @PostMapping("/regist")
    public void addCartItem(@ModelAttribute CartOrderDTO cartOrderDTO, HttpServletRequest request, HttpServletResponse response) {
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
        String memberId = "user1";

        cartOrderDTO.setMemberId(memberId);
        boolean cnt = cartService.cartCnt(cartOrderDTO);
        boolean added = cartService.regist(cartOrderDTO);

        if (!cnt) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("이미 장바구니에 존재하는 항목입니다.", request.getHeader("Referer"), response);
            return;
        }

        // 장바구니 담기 실패
        if (!added) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("장바구니 담기 실패. 다시 시도해주세요.", request.getHeader("Referer"), response);
            return;
        }

        // 장바구니에 상품 담기 성공
        response.setCharacterEncoding("utf-8");
        JSFunc.alertLocation("장바구니에 상품 담기 성공", "/cart/list", response);
    }

    // 장바구니 리스트 확인
    @GetMapping("/list")
    public String cartList(Model model) {
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
        String memberId = "user1";
        List<CartOrderDTO> cartList = cartService.cartList(memberId);
//        log.info("cartList{}", cartList);
        model.addAttribute("cartList", cartList);
        return "mypage/cartList";
    }

    // 결제할 강좌 리스트
//    @PostMapping("/order")
//    public String goToCheckout(@RequestParam List<Integer> idxList, @RequestParam("memberId") String memberId, Model model) {
//        List<CartOrderDTO> selectedItems = cartService.goOrder(idxList, memberId);
//        model.addAttribute("selectedItems", selectedItems);
//        return "checkout";
//    }

    // 장바구니 항목 삭제
    @PostMapping("/delete")
    @ResponseBody
    public String deleteCartItem(@RequestBody Map<String, List<String>> requestData, HttpServletRequest request) {
        List<String> idxListString = requestData.get("idxList");
        List<Integer> idxList = idxListString.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
//        log.info("삭제할 항목들: {}", idxList);
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
        String memberId = "user1";
        boolean success = false;
        try {
            cartService.delete(idxList, memberId);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", success ? "삭제 성공!" : "삭제 실패!");
        return jsonResponse.toString();
    }
}