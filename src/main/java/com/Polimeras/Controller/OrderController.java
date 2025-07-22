package com.Polimeras.Controller;

import com.Polimeras.Entity.Orders;
import com.Polimeras.Entity.Users;
import com.Polimeras.Service.OrderService;
import com.Polimeras.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')") // fixed syntax
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    // ✅ API to get all orders for logged-in user
    @GetMapping("/orders")
    public ResponseEntity<?> viewOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        List<Orders> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }

    // ✅ API to get specific order by ID
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> viewOrderDetails(@PathVariable Long orderId,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        Orders order = orderService.findById(orderId)
                .orElse(null);

        if (order == null) {
            return ResponseEntity.status(404).body("Order not found");
        }

        if (!(order.getUser().getId()==user.getId())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(order);
    }
}
