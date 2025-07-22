package com.Polimeras.Controller;

import com.Polimeras.Entity.OrderStatus;
import com.Polimeras.Entity.Orders;
import com.Polimeras.Entity.PaymentStatus;
import com.Polimeras.Entity.Users;
import com.Polimeras.Repository.OrdersRepository;
import com.Polimeras.Repository.PaymentGateway;
import com.Polimeras.Service.OrderService;
import com.Polimeras.Service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
//import java.util.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentGateway paymentGateway;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    private final String successUrl = "http://localhost:8080/api/payment/success";
    private final String cancelUrl = "http://localhost:8080/api/payment/cancel";
//    private final String successUrl = "http://localhost:8080/api/paypal/success";
//    private final String cancelUrl = "http://localhost:8080/api/paypal/cancel";

//    public static class URLUtils {
//
//        public static String getBaseUrl(HttpServletRequest request) {
//            String scheme = request.getScheme() + "://";
//            String serverName = request.getServerName();
//            String serverPort = (request.getServerPort() == 80 || request.getServerPort() == 443)
//                    ? ""
//                    : ":" + request.getServerPort();
//            String contextPath = request.getContextPath();
//
//            return scheme + serverName + serverPort + contextPath;
//        }
//
//        public static String getFullUrl(HttpServletRequest request) {
//            return getBaseUrl(request) + request.getRequestURI() +
//                    (request.getQueryString() != null ? "?" + request.getQueryString() : "");
//        }
//    }

    @PostMapping("/pay")
    public String makePayment(@RequestParam double amount) throws PayPalRESTException {
        Payment payment = paymentGateway.createPayment(
                amount,
                "USD",
                "paypal",
                "sale",
                "payment description",
                cancelUrl,
                successUrl);

        for (Links links : payment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                return "Redirect to: " + links.getHref();
            }
        }
        return "Error Processing the payment";
    }

//    @GetMapping("/success")
//    public String success(@RequestParam("paymentId") String paymentID,
//                                 @RequestParam("PayerID") String payerID,
//                                 @RequestParam("token") String paypalToken,
//                                 @AuthenticationPrincipal UserDetails userDetails) throws PayPalRESTException {
//
//        Payment payment = paymentGateway.execute(paymentID, payerID);
//        System.out.println("paymentId : " + paymentID);
//        System.out.println("payerId : " + payerID);
//        System.out.println("Token : " + paypalToken);
//
//        if (payment.getState().equals("approved")) {
//            Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
//            if (user != null) {
//                Orders order = orderService.getLatestPendingOrderForUser(user);
//                if (order != null) {
//                    order.setStatus(OrderStatus.CONFIRMED);
//                    order.setPaymentStatus(PaymentStatus.PAID);
//                    ordersRepository.save(order);
//                }
//            }
//
//            return "Payment successfully placed✅" ;
//        }
//
//        return "Payment Failed";
//    }

//
//    @GetMapping("/cancel")
//    public String cancel() {
//        return "Payment cancelled ❌";
//    }

    //    @GetMapping("/success")
//    public String success(@RequestParam("paymentId") String paymentID,
//                          @RequestParam("PayerID") String payerID,
//                          @AuthenticationPrincipal UserDetails userDetails) throws PayPalRESTException {
//        Payment payment = paymentGateway.execute(paymentID, payerID);
//        System.out.println("paymentId : " + paymentID);
//        System.out.println("payerId : " + payerID);
////        Orders order = orderService.getUserOrders(userService.findByUsername(userDetails.getUsername()).orElse(null));
////        System.out.println(order.getOrderNumber());
//        if (payment.getState().equals("approved")) {
//            return "Payment is successfully done ✅";
//        }
//        return "Payment Failed";
//    }

}
