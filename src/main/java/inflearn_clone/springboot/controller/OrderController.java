package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.dto.order.OrderCourseDTO;
import inflearn_clone.springboot.dto.order.OrderDTO;
import inflearn_clone.springboot.service.order.OrderService;
import inflearn_clone.springboot.utils.JSFunc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/regist")
    public void registOrder(
            HttpServletRequest request, HttpServletResponse response) {
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
        String memberId = "user1";

        boolean success = orderService.processOrder(memberId);
        if (success) {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("주문이 성공적으로 처리되었습니다.", "/mypage/courseList", response);
        } else {
            response.setCharacterEncoding("utf-8");
            JSFunc.alertLocation("주문 처리에 실패했습니다. 다시 시도해주세요.", request.getHeader("Referer"), response);
        }
    }


    @GetMapping("/list")
    public List<OrderCourseDTO> orderList(@RequestParam String memberId) {
        return orderService.getOrderList(memberId);
    }
}


