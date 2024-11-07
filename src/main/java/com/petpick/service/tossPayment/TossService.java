package com.petpick.service.tossPayment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String CANCEL_URL_TEMPLATE = "https://api.tosspayments.com/v1/payments/%s/cancel";

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
    public int confirmPayment(PaymentSuccessRequest request, Integer userId) {
        RestTemplate restTemplate = new RestTemplate();

        // Encode the secret key to Base64, appending ':' to represent empty password
        String encodedSecretKey = Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        // Log the encoded secret key for verification
        System.out.println("Encoded Secret Key: " + encodedSecretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request payload
        TossPaymentConfirmRequest tossRequest = new TossPaymentConfirmRequest(
                request.getPaymentKey(),
                request.getOrderSerialCode(),
                request.getAmount()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(tossRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return 0;
        }

        // Log for debugging
        System.out.println("Confirmation URL: " + CONFIRM_URL);
        System.out.println("JSON Payload: " + jsonPayload);
        System.out.println("Headers: " + headers);

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(CONFIRM_URL, entity, String.class);

            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                // Payment confirmed, proceed with order processing

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
                            .user(User.builder().userId(userId).build())
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
                        .user(User.builder().userId(userId).build())
                        .address(address)
                        .ordersPrice(request.getAmount())
                        .paymentKey(request.getPaymentKey())
                        .ordersRequest(request.getOrderRequest())
                        .ordersSerialCode(request.getOrderSerialCode())
                        .ordersStatus(OrderStatus.ORDER_CONFIRM)
                        .build();

                ordersRepository.save(order);

                List<OrderDetailRequest> orderDetailRequests = request.getOrderDetails();
                List<OrderDetail> orderDetails = new ArrayList<>();

                for (OrderDetailRequest detailRequest : orderDetailRequests) {
                    Optional<Product> optionalProduct = productRepository.findById(detailRequest.getProductId());
                    if (!optionalProduct.isPresent()) {
                        throw new RuntimeException("Product not found with ID: " + detailRequest.getProductId());
                    }
                    Product product = optionalProduct.get();

                    OrderDetail orderDetail = OrderDetail.builder()
                            .orders(order)
                            .product(product)
                            .orderDetailPrice(detailRequest.getOrderDetailPrice())
                            .orderDetailCnt(detailRequest.getOrderDetailCnt())
                            .orderDetailStatus(OrderDetailStatus.PAY_CONFIRM)
                            .build();

                    orderDetails.add(orderDetail);
                }

                orderDetailRepository.saveAll(orderDetails);

                return order.getOrdersId();
            } else {
                System.out.println("Payment confirmation failed with status code: " + response.getStatusCode());
                return 0;
            }
        } catch (HttpClientErrorException e) {
            System.out.println("HttpClientErrorException caught");
            System.out.println("Status code: " + e.getStatusCode());
            System.out.println("Response body: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public boolean cancelPayment(PaymentCancelRequest request, Integer userId) {
        RestTemplate restTemplate = new RestTemplate();

        // Encode the secret key to Base64, appending ':' to represent empty password
        String encodedSecretKey = Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        // Log the encoded secret key for verification
        System.out.println("Encoded Secret Key: " + encodedSecretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Retrieve the order detail to cancel
        Optional<OrderDetail> optionalOrderDetail = orderDetailRepository.findById(request.getOrderDetailId());
        if (!optionalOrderDetail.isPresent()) {
            throw new RuntimeException("Order Detail not found with ID: " + request.getOrderDetailId());
        }
        OrderDetail orderDetail = optionalOrderDetail.get();

        // Verify that the order detail belongs to the user
        if (!orderDetail.getOrders().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Order Detail does not belong to the user.");
        }

        // Retrieve the order
        Orders order = orderDetail.getOrders();

        // Calculate the cancel amount
        int cancelAmount = orderDetail.getOrderDetailPrice() * request.getOrderDetailCnt();

        // Construct the cancellation URL
        String cancelUrl = String.format("https://api.tosspayments.com/v1/payments/%s/cancel", order.getPaymentKey());

        // Prepare the JSON payload for cancellation, including cancelAmount
        Map<String, Object> cancelRequestMap = new HashMap<>();
        cancelRequestMap.put("cancelReason", request.getCancelReason());
        cancelRequestMap.put("cancelAmount", cancelAmount);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(cancelRequestMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

        // Log for debugging
        System.out.println("Cancellation URL: " + cancelUrl);
        System.out.println("JSON Payload: " + jsonPayload);
        System.out.println("Headers: " + headers);

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(cancelUrl, entity, String.class);

            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {

                // Update the order detail count
                orderDetail.decreaseOrderDetailCnt(request.getOrderDetailCnt());
                if (orderDetail.isOrderDetailCntZero()) {
                    // Delete the order detail if count is zero
                    orderDetailRepository.delete(orderDetail);
                } else {
                    orderDetailRepository.save(orderDetail);
                }

                // Update the orders_price in the Orders table
                order.decreaseOrdersPrice(cancelAmount);
                ordersRepository.save(order);

                // Optionally, update the product inventory
                Product product = orderDetail.getProduct();
                product.increaseProductCnt(request.getOrderDetailCnt());
                productRepository.save(product);

                // New Condition: Check if there are no remaining OrderDetails or if ordersPrice is zero
                List<OrderDetail> remainingOrderDetails = orderDetailRepository.findByOrders(order);
                if (remainingOrderDetails.isEmpty() || order.getOrdersPrice() == 0) {
                    // Delete the Orders entry
                    ordersRepository.delete(order);
                }

                return true;
            } else {
                System.out.println("Payment cancellation failed with status code: " + response.getStatusCode());
                return false;
            }
        } catch (HttpClientErrorException e) {
            System.out.println("HttpClientErrorException caught during cancellation");
            System.out.println("Status code: " + e.getStatusCode());
            System.out.println("Response body: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Exception caught during cancellation");
            e.printStackTrace();
            return false;
        }
    }

}
