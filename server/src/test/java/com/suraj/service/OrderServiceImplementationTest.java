package com.suraj.service;

import com.suraj.exception.OrderException;
import com.suraj.modal.*;
import com.suraj.modal.Order;
import com.suraj.repository.*;
import com.suraj.user.domain.OrderStatus;
import com.suraj.user.domain.PaymentStatus;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplementationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImplementation orderService;

    private User testUser;
    private Address testAddress;
    private Cart testCart;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a mock user, address, and cart for testing
        testUser = new User();
        testUser.setId(1L);

        testAddress = new Address();
        testAddress.setUser(testUser);

        testCart = new Cart();
//      testCart.setUserId(testUser.getId());
        testCart.setTotalPrice(100.0);
        testCart.setTotalDiscountedPrice(90);
        testCart.setTotalItem(2);
        testCart.setDiscounte(10);

        CartItem item = new CartItem();
        item.setPrice(50);
        item.setDiscountedPrice(45);
        item.setProduct(new Product());
        item.setQuantity(2);

        testCart.getCartItems().add(item);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setOrderStatus(OrderStatus.PENDING);
        testOrder.setTotalPrice(100.0);
        testOrder.setTotalDiscountedPrice(90);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrder() {
        when(cartService.findUserCart(testUser.getId())).thenReturn(testCart);
        when(addressRepository.save(testAddress)).thenReturn(testAddress);
        when(userRepository.save(testUser)).thenReturn(testUser);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUser(testUser);
        savedOrder.setOrderStatus(OrderStatus.PENDING);
        savedOrder.setTotalPrice(100.0);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

//        Order order = orderService.createOrder(testUser, testAddress);

//        assertNotNull(order);
//        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
        //assertEquals(testUser, order.getUser());
//        /assertEquals(100.0, order.getTotalPrice());
    }

    @Test
    void placedOrder() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order placedOrder = orderService.placedOrder(1L);

        assertEquals(OrderStatus.PLACED, placedOrder.getOrderStatus());
        assertEquals(PaymentStatus.COMPLETED, placedOrder.getPaymentDetails().getStatus());
    }

    @Test
    void confirmedOrder() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order confirmedOrder = orderService.confirmedOrder(1L);

        assertEquals(OrderStatus.CONFIRMED, confirmedOrder.getOrderStatus());
    }

    @Test
    void shippedOrder() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order shippedOrder = orderService.shippedOrder(1L);

        assertEquals(OrderStatus.SHIPPED, shippedOrder.getOrderStatus());
    }

//    @Test
//    void deliveredOrder() throws OrderException {
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
//
//        Order deliveredOrder = orderService.deliveredOrder(1L);
//
//        assertEquals(OrderStatus.DELIVERED, deliveredOrder.getOrderStatus());
//    }

    @Test
    void cancledOrder() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order canceledOrder = orderService.cancledOrder(1L);

        assertEquals(OrderStatus.CANCELLED, canceledOrder.getOrderStatus());
    }

    @Test
    void findOrderById() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order foundOrder = orderService.findOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
    }

    @Test
    void findOrderByIdThrowsExceptionWhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderException thrown = assertThrows(OrderException.class, () -> orderService.findOrderById(1L));
        assertEquals("order not exist with id 1", thrown.getMessage());
    }

    @Test
    void usersOrderHistory() {
        Order order = new Order();
        order.setId(1L);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        when(orderRepository.getUsersOrders(testUser.getId())).thenReturn(orderList);

        List<Order> orders = orderService.usersOrderHistory(testUser.getId());

        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    void getAllOrders() {
        Order order = new Order();
        order.setId(1L);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(orders);

        List<Order> allOrders = orderService.getAllOrders();

        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
    }

    @Test
    void deleteOrder() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrderThrowsExceptionWhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderException thrown = assertThrows(OrderException.class, () -> orderService.deleteOrder(1L));
        assertEquals("order not exist with id 1", thrown.getMessage());
    }
}
