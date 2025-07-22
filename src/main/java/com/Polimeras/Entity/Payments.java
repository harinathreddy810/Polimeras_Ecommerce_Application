package com.Polimeras.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payments {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    @Column(insertable=false, updatable=false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Orders orderId;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    //	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TransId")
//	@SequenceGenerator(name = "TransId" ,sequenceName="1000",allocationSize = 1)
    private int transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentsStatus paymentStatus;

    private LocalDateTime paymentDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

//    Getters and Setters


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Orders getOrderId() {
        return orderId;
    }

    public void setOrderId(Orders orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentsStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentsStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}