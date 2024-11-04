package com.petpick.service.tossPayment;

import com.petpick.domain.Orders;
import com.petpick.domain.User;
import com.petpick.model.PaymentSuccessRequest;
import com.petpick.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class TossService {

    @Value("${payment.toss.test_secret_api_key}")
    private String tossSecretKey;

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final OrderRepository ordersRepository;

    public TossService(OrderRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Transactional
    public boolean confirmPayment(PaymentSuccessRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Encode the secret key to Base64
        String encodedSecretKey = Base64.getEncoder()
                .encodeToString(tossSecretKey.getBytes(StandardCharsets.UTF_8));

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the JSON payload
        String jsonPayload = String.format(
                "{\"paymentKey\":\"%s\",\"amount\":%d,\"orderId\":\"%s\"}",
                request.getPaymentKey(),
                request.getAmount(),
                request.getOrderId()
        );

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(CONFIRM_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Payment confirmed, save to the database
                Orders order = Orders.builder()
                        .user(User.builder().userId(request.getUserId()).build()) // Assuming User has a builder and id
                        .ordersPrice(request.getAmount())
                        .paymentKey(request.getPaymentKey())
                        .ordersRequest(request.getOrderRequest())
                        .ordersSerialCode(request.getOrderSerialCode())
                        .build();

                ordersRepository.save(order);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // Handle exceptions such as RestClientException
            e.printStackTrace();
            return false;
        }
    }
}
