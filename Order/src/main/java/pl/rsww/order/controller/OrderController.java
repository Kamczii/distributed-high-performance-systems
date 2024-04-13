package pl.rsww.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import pl.rsww.order.request.OrderRequest;
import pl.rsww.order.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(path="order/create")
    public void createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
    }
}
