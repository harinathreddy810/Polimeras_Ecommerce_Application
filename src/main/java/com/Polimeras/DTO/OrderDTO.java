package com.Polimeras.DTO;

import java.time.LocalDateTime;
import com.Polimeras.Entity.Orders;
import com.Polimeras.Entity.OrderStatus;
import com.Polimeras.Entity.PaymentStatus;

public class OrderDTO {
    private String orderNumber;
    private LocalDateTime createdAt;
    private String customerName;
    private double totalAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;

    public OrderDTO(String orderNumber, LocalDateTime createdAt, String customerName, 
                   double totalAmount, OrderStatus status, PaymentStatus paymentStatus) {
        this.orderNumber = orderNumber;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    // Static factory method
    public static OrderDTO fromEntity(Orders order) {
        String customerName = order.getUser() != null ? 
            order.getUser().getFirstname() + " " + order.getUser().getLastname() : "Unknown";
            
        return new OrderDTO(
            order.getOrderNumber(),
            order.getCreatedAt(),
            customerName,
            order.getTotalAmount(),
            order.getStatus(),
            order.getPaymentStatus()
        );
    }

    // Getters and setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
}