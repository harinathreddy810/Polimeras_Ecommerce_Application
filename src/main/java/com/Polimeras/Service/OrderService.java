package com.Polimeras.Service;

import com.Polimeras.Entity.*;
import com.Polimeras.Repository.OrdersRepository;
import com.Polimeras.Repository.ProductsRepository;
import jakarta.persistence.criteria.Order;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductService productService;
    
    


//    @Transactional
//    public Orders createOrder(Users user, PaymentMethod paymentMethod, String shippingAddress) {
//        Cart cart = cartService.getOrCreateCart(user);
//
//        if (cart.getCartItems().isEmpty()) {
//            throw new IllegalStateException("Cannot create order from empty cart");
//        }
//
//        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//
//        // Create order
//        Orders order = new Orders(
//                user,
//                orderNumber,
//                cart.getTotalAmount(),
//                paymentMethod,
//                shippingAddress
//        );
//
//        // Convert cart items to order items
//        Set<OrderItems> orderItems = new HashSet<>();
//        for (CartItems cartItem : cart.getCartItems()) {
//            Products product = cartItem.getProduct();
//
//            // Check stock availability
//            if (product.getStockQuantity() < cartItem.getQuantity()) {
//                throw new IllegalStateException(
//                        "Insufficient stock for product: " + product.getName());
//            }
//
//            // Reduce product quantity
//            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
//            productService.updateProduct(product);
//
//            // Create order item
//            OrderItems orderItem = new OrderItems(
//                    order,
//                    product,
//                    cartItem.getQuantity(),
//                    cartItem.getPrice()
//            );
//            orderItems.add(orderItem);
//        }
//
//        order.setOrderItems(orderItems);
//
//        // Save order
//        Orders savedOrder = ordersRepository.save(order);
//
//        // Clear cart
//        cartService.clearCart(user);
//
//        return savedOrder;
//    }


    @Transactional
    public Orders createOrder(Users user, String  paymentMethod, String shippingAddress,OrderStatus orderStatus,PaymentStatus paymentStatus) {
        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from empty cart");
        }

        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        double totalAmount = cart.getTotalAmount(); // Total cart amount
        double priceAtPurchase = totalAmount;       // ✅ Set price at purchase same as total

        // ✅ Pass priceAtPurchase to constructor
        Orders order = new Orders(
                user,
                orderNumber,
                cart.getTotalAmount(),
                paymentMethod,
                shippingAddress

        );

        //setting order status manually
        order.setStatus(orderStatus);
        order.setPaymentStatus(paymentStatus);

        // Convert cart items to order items
        Set<OrderItems> orderItems = new HashSet<>();
        for (CartItems cartItem : cart.getCartItems()) {
            Products product = cartItem.getProduct();

            // Check stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: " + product.getName());
            }

            // Reduce product quantity
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productService.updateProduct(product);

            // Create order item
            OrderItems orderItem = new OrderItems(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getPrice()
            );
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        // Save order
        Orders savedOrder = ordersRepository.save(order);

        // Clear cart
        cartService.clearCart(user);

        return savedOrder;
    }


    public List<Orders> getUserOrders(Users user) {
        return ordersRepository.findByUser(user);
    }

    public Optional<Orders> findById(Long id) {
        return ordersRepository.findById(id);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findByOrderByCreatedAtDesc();
    }

    public Orders updateOrderStatus(Long orderId, OrderStatus status) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        return ordersRepository.save(order);
    }

    public Orders getLatestPendingOrderForUser(Users user) {
        return ordersRepository.findTopByUserAndStatusOrderByCreatedAtDesc(user, OrderStatus.PENDING);
    }

    // ✅ Newly added method
    public Orders updateOrder(Orders order ) {
        Orders order1 = ordersRepository.save(order);
        Users user = order1.getUser();
//        emailService.sendSuccessEmail(order1.getOrderNumber(),order1.getTotalAmount(),user.getEmail(),order1.getShippingAddress());
        System.out.println(user);
        return order1 ;
    }
    
    //newly added
    public List<Orders> getVendorOrders(Users vendor) {
        // This assumes you have a way to link orders to products to vendors
        // You might need to modify your repository query
        return ordersRepository.findByOrderItems_Product_User(vendor);
    }
    
}
