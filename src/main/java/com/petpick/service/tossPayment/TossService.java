package com.petpick.service.tossPayment;

import com.petpick.domain.*;
import com.petpick.domain.type.OrderDetailStatus;
import com.petpick.domain.type.OrderStatus;
import com.petpick.model.*;
import com.petpick.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class TossService {

    @Value("${payment.toss.test_secret_api_key}")
    private String tossSecretKey;

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final OrderRepository ordersRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AddressRepository addressRepository;

    public TossService(OrderRepository ordersRepository,
                       ProductRepository productRepository,
                       OrderDetailRepository orderDetailRepository,
                       AddressRepository addressRepository) {
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public boolean confirmPayment(PaymentSuccessRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        String encodedSecretKey = Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonPayload = String.format(
                "{\"paymentKey\":\"%s\",\"amount\":%d,\"orderId\":\"%s\"}",
                request.getPaymentKey(),
                request.getAmount(),
                request.getOrderSerialCode()
        );

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(CONFIRM_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Payment confirmed, save to the database

                // Process Address
                AddressRequest addressRequest = request.getAddress();
                Address address;

                if (addressRequest.getAddressId() != null) {
                    // Fetch existing address
                    Optional<Address> optionalAddress = addressRepository.findById(addressRequest.getAddressId());
                    if (!optionalAddress.isPresent()) {
                        throw new RuntimeException("Address not found with ID: " + addressRequest.getAddressId());
                    }
                    address = optionalAddress.get();
                } else {
                    address = Address.builder()
                            .user(User.builder().userId(addressRequest.getUserId()).build())
                            .addressName(addressRequest.getAddressName())
                            .addressZipcode(addressRequest.getAddressZipcode())
                            .addressAddr(addressRequest.getAddressAddr())
                            .addressAddrDetail(addressRequest.getAddressAddrDetail())
                            .addressTel(addressRequest.getAddressTel())
                            .addressRequest(addressRequest.getAddressRequest())
                            .addressDefault(addressRequest.getAddressDefault())
                            .build();
                    addressRepository.save(address);
                }

                Orders order = Orders.builder()
                        .user(User.builder().userId(request.getUserId()).build())
                        .address(address)
                        .ordersPrice(request.getAmount())
                        .paymentKey(request.getPaymentKey())
                        .ordersRequest(request.getOrderRequest())
                        .ordersSerialCode(request.getOrderSerialCode())
                        .ordersStatus(OrderStatus.ORDER_CONFIRM)
                        .build();

                ordersRepository.save(order);

                List<OrderDetailResponse> orderDetailResponses = request.getOrderDetails();
                List<OrderDetail> orderDetails = new ArrayList<>();

                for (OrderDetailResponse detailResponse : orderDetailResponses) {
                    Optional<Product> optionalProduct = productRepository.findById(detailResponse.getProductId());
                    if (!optionalProduct.isPresent()) {
                        throw new RuntimeException("Product not found with ID: " + detailResponse.getProductId());
                    }
                    Product product = optionalProduct.get();

                    OrderDetail orderDetail = OrderDetail.builder()
                            .orders(order)
                            .product(product)
                            .orderDetailPrice(detailResponse.getOrderDetailPrice())
                            .orderDetailCnt(detailResponse.getOrderDetailCnt())
                            .orderDetailStatus(OrderDetailStatus.PAY_CONFIRM)
                            .build();

                    orderDetails.add(orderDetail);
                }

                orderDetailRepository.saveAll(orderDetails);

                return true;
            } else {
                System.out.println("Payment confirmation failed with status code: " + response.getStatusCode());
                return false;
            }
        } catch (HttpClientErrorException e) {
            System.out.println("HttpClientErrorException caught");
            System.out.println("Status code: " + e.getStatusCode());
            System.out.println("Response body: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
