package com.petpick.service.tossPayment;

import com.petpick.domain.Orders;
import com.petpick.domain.User;
import com.petpick.repository.OrderRepository;
import com.petpick.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class TossService {

    @Value("${payment.toss.test_secret_api_key}")
    private String tossSecretKey;

    public static final String URL = "https://api.tosspayments.com/v1/payments/confirm";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void confirmPayment(String orderId, String paymentKey, int amount, int userId, String orderRequest) throws Exception {
        // Get user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // Prepare authorization header
        String secretKeyWithColon = tossSecretKey + ":";
        String encodedAuth = Base64.getEncoder()
                .encodeToString(secretKeyWithColon.getBytes(StandardCharsets.UTF_8));

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare request body
        TossPaymentRequest tossPaymentRequest = new TossPaymentRequest(paymentKey, amount, orderId);

        HttpEntity<TossPaymentRequest> requestEntity = new HttpEntity<>(tossPaymentRequest, headers);

        RestTemplate restTemplate = new RestTemplate();

        // Make the POST request to Toss
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Save data into 'order' table
            Orders order = Orders.builder()
                    .user(user)
                    .ordersPrice(amount)
                    .paymentKey(paymentKey)
                    .ordersRequest(orderRequest)
                    .build();
            orderRepository.save(order);
        } else {
            // Handle error response
            throw new Exception("Failed to confirm payment with Toss");
        }
    }
}
