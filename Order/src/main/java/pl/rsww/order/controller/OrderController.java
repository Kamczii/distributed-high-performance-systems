package pl.rsww.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import pl.rsww.order.request.OrderRequest;
import pl.rsww.order.service.OrderService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(path="order/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("userId") Long userId) {
        UUID orderId = orderService.createOrder(orderRequest, userId);
        return ResponseEntity.ok(orderId.toString());
    }
}

