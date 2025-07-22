package com.Polimeras.Repository;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public interface PaymentGateway {
    String display();

    Payment createPayment(Double total,String currency,String method,String intent,
                          String description,String cancelUrl,String SuccessUrl) throws PayPalRESTException;

    Payment execute(String paymentId,String payerId) throws PayPalRESTException;
}
