package pl.rsww.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rsww.payment.request.PaymentRequest;
import pl.rsww.payment.service.PaymentService;

import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping(path="payment/accept")
    public ResponseEntity<String> acceptPayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader("userId") Long userId) {
        Boolean result = paymentService.acceptPayment(paymentRequest, userId);
        return ResponseEntity.ok(result.toString());
    }

    @PostMapping(path="payment/cancel")
    public void cancelPayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader("userId") Long userId) {
        paymentService.cancelPayment(paymentRequest, userId);
    }
}

