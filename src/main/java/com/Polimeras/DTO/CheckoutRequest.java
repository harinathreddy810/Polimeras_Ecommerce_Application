package com.Polimeras.DTO;

import com.Polimeras.Entity.PaymentMethod;

public class CheckoutRequest {
    PaymentMethod paymentMethod;
    String address;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
