package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
import inflearn_clone.springboot.service.cart.CartService;
import inflearn_clone.springboot.service.order.OrderService;
import inflearn_clone.springboot.utils.JSFunc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping("/regist")
    public void registOrder(@RequestParam List<Integer> courseIdxList,
                            @RequestParam List<Integer> priceList,
                            HttpServletRequest request, HttpServletResponse response) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        String orderNumber = generateOrderNumber();

        List<Integer> successOrderedList = new ArrayList<>();
        List<Integer> alreadyOrderedList = new ArrayList<>();
        log.info("courseIdxList{}: ", courseIdxList);
        for (int i = 0; i < courseIdxList.size(); i++) {
            int courseIdx = courseIdxList.get(i);
            int price = priceList.get(i);

            boolean isOrderPossible = orderService.orderCnt(courseIdx, memberId);
            if (isOrderPossible) {
                // 결제 가능한 경우
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setMemberId(memberId);
                orderDTO.setCourseIdx(courseIdx);
                orderDTO.setOrderPrice(price);
                orderDTO.setOrderNumber(orderNumber);

                orderService.regist(orderDTO);
                successOrderedList.add(courseIdx);
            } else {
                log.info(courseIdx);
                alreadyOrderedList.add(courseIdx);
            }
        }

        if (!successOrderedList.isEmpty()) {
            cartService.delete(successOrderedList, memberId);

            String message = "결제가 완료되었습니다.";
            if (!alreadyOrderedList.isEmpty()) {
                message += " 이미 결제된 강좌는 제외되었습니다.";
            }

            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation(message, "member/myPage", response);
        } else {
            String message = "결제 가능한 강좌가 없습니다.";
            if (!alreadyOrderedList.isEmpty()) {
                log.info(alreadyOrderedList);
                message = "모든 강좌가 이미 결제된 상태입니다.";
            }
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation(message, request.getHeader("Referer"), response);
        }
    }

    private String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        int randomNum = (int) (Math.random() * 1000000);

        return currentDate + "_" + String.format("%06d", randomNum);
    }

    @GetMapping("/list")
    public String orderList(Model model, HttpServletRequest request) {
        String memberId = (String) request.getSession().getAttribute("memberId");
        List<OrderCourseDTO> orderList = orderService.getOrderList(memberId);
        log.info("orderList{}",orderList);
        model.addAttribute("orderList", orderList);
        return "mypage/orderList";
    }

    @PostMapping("/refund")
    @ResponseBody
    public String refundOrder(@RequestParam("idx") int idx) {
        boolean result = orderService.refundOrder(idx);
        return result ? "F" : "S";
    }

    //============페이지테스트==============
    @GetMapping("/courseList")
    public String courseList() {
        return "mypage/courseList";
    }

//    @GetMapping("/orderList")
//    public String orderList() {
//        return "mypage/orderList";
//    }


}


