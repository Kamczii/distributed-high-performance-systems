package pl.rsww.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.rsww.payment.request.PaymentRequest;
import pl.rsww.payment.service.PaymentService;

@Slf4j
@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/payment/accept")
    public ResponseEntity<String> acceptPayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader("userId") Long userId) {
        Boolean result = paymentService.acceptPayment(paymentRequest, userId);
        return ResponseEntity.ok(result.toString());
    }

    @PostMapping("/payment/cancel")
    public void cancelPayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader("userId") Long userId) {
        paymentService.cancelPayment(paymentRequest, userId);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptPayment2(@RequestBody PaymentRequest paymentRequest, @RequestHeader("userId") Long userId) {
        Boolean result = paymentService.acceptPayment(paymentRequest, userId);
        return ResponseEntity.ok(result.toString());
    }

    @PostMapping("/cancel")
    public void cancelPayment2(@RequestBody PaymentRequest paymentRequest, @RequestHeader("userId") Long userId) {
        paymentService.cancelPayment(paymentRequest, userId);
    }
}

