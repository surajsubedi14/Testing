package com.suraj.controller;

import com.suraj.exception.OrderException;
import com.suraj.exception.UserException;
import com.suraj.modal.Order;
import com.suraj.repository.OrderRepository;
import com.suraj.response.ApiResponse;
import com.suraj.response.PaymentLinkResponse;
import com.suraj.service.OrderService;
import com.suraj.service.UserService;
import com.suraj.user.domain.OrderStatus;
import com.suraj.user.domain.PaymentStatus;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RazorpayClient razorpayClient;

    @InjectMocks
    private PaymentController paymentController;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order(); // Initialize order with necessary data for testing
        order.setTotalPrice(1000.0);
        order.setOrderId("12345");
        //order.setPaymentDetails(new PaymentDetails());
        // Setup additional necessary order properties
    }

    @AfterEach
    void tearDown() {
        // Clean up any resources if needed
    }

    @Test
    void createPaymentLink() throws RazorpayException, UserException, OrderException {
        // Arrange
        Long orderId = 1L;
        String jwt = "mockJwtToken";
        when(orderService.findOrderById(orderId)).thenReturn(order);
        RazorpayClient razorpayClientMock = mock(RazorpayClient.class);
//        PaymentLink paymentLink = mock(PaymentLink.class);
//        when(razorpayClientMock.paymentLink.create(any())).thenReturn(paymentLink);
//        when(paymentLink.get("id")).thenReturn("paymentLinkId");
//        when(paymentLink.get("short_url")).thenReturn("http://mock-payment-link-url");
//
//        // Act
//        ResponseEntity<PaymentLinkResponse> response = paymentController.createPaymentLink(orderId, jwt);
//
//        // Assert
//        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("http://mock-payment-link-url", response.getBody().getPaymentLinkUrl());
//        assertEquals("paymentLinkId", response.getBody().getPaymentLinkId());
    }

    @Test
    void redirect() throws RazorpayException, OrderException {
        // Arrange
        String paymentId = "mockPaymentId";
        Long orderId = 1L;
        Payment payment = mock(Payment.class);
        when(payment.get("status")).thenReturn("captured");
        RazorpayClient razorpayClientMock = mock(RazorpayClient.class);
//        when(razorpayClientMock.payments.fetch(paymentId)).thenReturn(payment);
//        when(orderService.findOrderById(orderId)).thenReturn(order);
//
//        // Act
//        ResponseEntity<ApiResponse> response = paymentController.redirect(paymentId, orderId);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("your order get placed", response.getBody().getMessage());
//        assertEquals(PaymentStatus.COMPLETED, order.getPaymentDetails().getStatus());
//        assertEquals(OrderStatus.PLACED, order.getOrderStatus());
    }

    @Test
    void redirectPaymentFailed() throws RazorpayException {
        // Arrange
        String paymentId = "mockPaymentId";
        Long orderId = 1L;
        Payment payment = mock(Payment.class);
        when(payment.get("status")).thenReturn("failed");
        RazorpayClient razorpayClientMock = mock(RazorpayClient.class);
//        when(razorpayClientMock.payments.fetch(paymentId)).thenReturn(payment);
//
//        // Act & Assert
//        RazorpayException exception = assertThrows(RazorpayException.class, () -> {
//            paymentController.redirect(paymentId, orderId);
//        });
//        assertTrue(exception.getMessage().contains("payment failed"));
    }
}
