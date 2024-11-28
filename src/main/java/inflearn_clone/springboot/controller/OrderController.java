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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/regist")
    public String registOrder(@RequestParam String memberId,
                              @RequestParam String courseIdxList,
                              @RequestParam String priceList) {
        // 로그인 후 삭제
        //String memberId = (String) request.getSession().getAttribute("memberId");
         memberId = "user1";
         String orderNumber = generateOrderNumber();


        String[] courseIdxArray = courseIdxList.split(",");
        String[] priceArray = priceList.split(",");

        for (int i = 0; i < courseIdxArray.length; i++) {
            int courseIdx = Integer.parseInt(courseIdxArray[i]);
            int price = Integer.parseInt(priceArray[i]);

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setMemberId(memberId);
            orderDTO.setCourseIdx(courseIdx);
            orderDTO.setOrderPrice(price);
            orderDTO.setOrderNumber(orderNumber);
            orderService.regist(orderDTO);
        }
        return "redirect:/mypage/courseList";
    }

    private String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        int randomNum = (int) (Math.random() * 1000000);

        return currentDate + "_" + String.format("%06d", randomNum);
    }

    @GetMapping("/list")
    public List<OrderCourseDTO> orderList(@RequestParam String memberId) {
        return orderService.getOrderList(memberId);
    }
}


