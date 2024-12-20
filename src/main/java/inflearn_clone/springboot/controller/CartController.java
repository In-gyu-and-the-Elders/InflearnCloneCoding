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
        String memberId = (String) request.getSession().getAttribute("memberId");

        cartOrderDTO.setMemberId(memberId);
        boolean cnt = cartService.cartCnt(cartOrderDTO.getCourseIdx(), cartOrderDTO.getMemberId());

        if (!cnt) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("이미 장바구니에 존재하는 항목입니다.", request.getHeader("Referer"), response);
            return;
        }

        boolean added = cartService.regist(cartOrderDTO);
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
    public String cartList(Model model, HttpServletRequest request) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        List<CartOrderDTO> cartList = cartService.cartList(memberId);
        int totalCount = cartService.getCartCount(memberId);
//        log.info("cartList{}", cartList);
        model.addAttribute("cartList", cartList);
        model.addAttribute("totalCount", totalCount);
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
        log.info("삭제할 항목들: {}", idxList);
        String memberId = (String) request.getSession().getAttribute("memberId");
        boolean success = false;
        for (Integer idx : idxList) {
            try {
                cartService.delete(idxList, memberId);
                success = true;
            } catch (Exception e) {
                log.error("삭제 실패 - idx: {}, error: {}", idx, e.getMessage(), e);
                success = false;
            }
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", success ? "삭제되었습니다" : "다시시도해주세요");
        return jsonResponse.toString();
    }

    @PostMapping("/deleteList")
    @ResponseBody
    public String deleteCartItemsByCourseIdx(@RequestBody Map<String, List<Integer>> requestData, HttpServletRequest request) {
        List<Integer> courseIdxList = requestData.get("idxList");
        String memberId = (String) request.getSession().getAttribute("memberId");

        boolean success = false;
        try {
            cartService.deleteByCourseIdx(courseIdxList, memberId);
            success = true;
        } catch (Exception e) {
            log.error("삭제 실패: {}", e.getMessage(), e);
        }

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", success ? "삭제되었습니다" : "다시 시도해주세요");
        return jsonResponse.toString();
    }
}