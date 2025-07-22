package com.Polimeras.Controller;

import com.Polimeras.Entity.OrderStatus;
import com.Polimeras.Entity.Orders;
import com.Polimeras.Entity.PaymentStatus;
import com.Polimeras.Entity.Users;
import com.Polimeras.Repository.OrdersRepository;
import com.Polimeras.Repository.PaymentGateway;
import com.Polimeras.Service.EmailService;
import com.Polimeras.Service.OrderService;
import com.Polimeras.Service.PdfService;
import com.Polimeras.Service.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;

@Controller
public class MVController {

    @Autowired
    private PaymentGateway paymentGateway;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PdfService pdfService;

//    @GetMapping("/api/payment/success")
//    public RedirectView paymentSuccess(@RequestParam("paymentId") String paymentID,
//                                       @RequestParam("PayerID") String payerID,
//                                       @RequestParam("token") String paypalToken,
//                                       @AuthenticationPrincipal UserDetails userDetails) throws PayPalRESTException, PayPalRESTException {
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
//
//                // Redirect to static success page with parameters
//                String redirectUrl = "/payment-success.html?paymentId=" + paymentID +
//                        "&PayerID=" + payerID +
//                        "&name=" + user.getFirstname();
//                return new RedirectView(redirectUrl);
//            }
//        }
//
//        return new RedirectView("/payment-failed.html");
//    }

//    public String generateHtmlWithData(Orders order, Users user) {
//
//        StringBuilder html = new StringBuilder();
//        html.append("<html><head><style>");
//        html.append("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }");
//        html.append("th, td { border: 1px solid black; padding: 8px; text-align: left; }");
//        html.append("th { background-color: #f2f2f2; }");
//        html.append(".order-section { margin-bottom: 30px; page-break-inside: avoid; }"); // Added page-break for printing
//        html.append(".section-title { font-weight: bold; margin: 15px 0 10px 0; font-size: 1.1em; }");
//        html.append(".customer-info { background-color: #f8f9fa; padding: 15px; margin-bottom: 15px; border-radius: 5px; }");
//        html.append("</style></head><body>");
//
//        // Header with logo
//        html.append("<div style=\"display: flex; align-items: center; justify-content: center; padding: 20px 0;\">")
//                .append("<img src=\"http://localhost:8081/uploads/Polimeras-logo-black.png\" width=\"150\">")
//                .append("</div>");
//        html.append("<hr>");
//        html.append("<h2 style='text-align:center; margin-bottom: 30px;'>Orders Report</h2>");
//        if (user != null) {
//            html.append("<div class='customer-info'>");
//            html.append("<div class='section-title'>Customer Information</div>");
//            html.append("<table>");
//            html.append("<tr><th style='width: 30%;'>Customer ID</th><td>").append(user.getId()).append("</td></tr>");
//            html.append("<tr><th>Name</th><td>").append(user.getFirstname()).append(" ").append(user.getLastname()).append("</td></tr>");
//            html.append("<tr><th>Phone</th><td>").append(user.getPhoneNumber()).append("</td></tr>");
//            html.append("<tr><th>Address</th><td>").append(user.getAddress()).append("</td></tr>");
//            html.append("<tr><th>City/State/Zip</th><td>")
//                    .append(user.getCity()).append(", ")
//                    .append(user.getState()).append(" ")
//                    .append(user.getZipcode())
//                    .append("</td></tr>");
//            html.append("</table>");
//            html.append("</div>");
//        }
//
//        html.append("<div class='order-section'>");
//        html.append("<div class='section-title'>Order Details</div>");
//        html.append("<table>");
//        html.append("<tr><th style='width: 30%;'>Order Number</th><td>").append(order.getOrderNumber()).append("</td></tr>");
//        html.append("<tr><th>Total Amount</th><td>").append(String.format("$%.2f", order.getTotalAmount())).append("</td></tr>");
//        html.append("<tr><th>Payment Method</th><td>").append(order.getPaymentMethod()).append("</td></tr>");
//        html.append("<tr><th>Order Status</th><td>").append(order.getStatus()).append("</td></tr>");
//        html.append("<tr><th>Payment Status</th><td>").append(order.getPaymentStatus()).append("</td></tr>");
//        html.append("<tr><th>Shipping Address</th><td>").append(order.getShippingAddress()).append("</td></tr>");
//        html.append("</table>");
//
//        html.append("</div>");
//        html.append("<hr style='margin: 30px 0;'>");
//
//        html.append("</body></html>");
//
//        return html.toString();
//    }

    public String generateHtmlWithData(Orders order, Users user) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'><title>Invoice - Polimeraas</title>");
        html.append("<style>");
        html.append("body { font-family: 'Poppins', sans-serif; margin: 40px; color: #000; }");
        html.append(".header-logo { text-align: center; margin-bottom: 30px; }");
        html.append(".header-logo img { width: 160px; }");
        html.append(".section { margin-bottom: 30px; }");
        html.append(".section h2 { margin-bottom: 10px; }");
        html.append(".info-grid { display: flex; flex-wrap: wrap; gap: 20px; }");
        html.append(".info-box { flex: 1; min-width: 250px; }");
        html.append(".highlight { display: inline-block; background-color: #fff3cd; padding: 4px 10px; border-radius: 4px; font-weight: bold; }");
        html.append(".label { font-weight: bold; display: block; margin-bottom: 4px; }");
        html.append(".value { margin-bottom: 10px; }");
        html.append("hr { margin: 30px 0; }");
        html.append("</style></head><body>");

        // Header with logo
        html.append("<div class='header-logo'>")
                .append("<img src='http://localhost:8080/uploads/Polimeras-logo-black.png' alt='Polimeraas Logo'>")
                .append("</div>");

        // ✅ CUSTOMER INFORMATION FIRST
        if (user != null) {
            html.append("<div class='section'>")
                    .append("<h2>Customer Information</h2>")
                    .append("<div class='info-grid'>");

            html.append("<div class='info-box'>")
                    .append("<div class='label'>Customer ID:</div><div class='value'>").append(user.getId()).append("</div>")
                    .append("<div class='label'>Name:</div><div class='value'>").append(user.getFirstname()).append(" ").append(user.getLastname()).append("</div>")
                    .append("<div class='label'>Phone:</div><div class='value'>").append(user.getPhoneNumber()).append("</div>")
                    .append("</div>");

            html.append("<div class='info-box'>")
                    .append("<div class='label'>Address:</div><div class='value'>").append(user.getAddress()).append("</div>")
                    .append("<div class='label'>City/State/Zip:</div><div class='value'>")
                    .append(user.getCity()).append(", ").append(user.getState()).append(" - ").append(user.getZipcode()).append("</div>")
                    .append("</div>");

            html.append("</div></div>");
        }

        html.append("<hr>");

        // ✅ ORDER DETAILS NEXT
        html.append("<h2 style='text-align:center;'>Order <span class='highlight'>#").append(order.getOrderNumber()).append("</span></h2>");

        html.append("<div class='section info-grid'>");

        html.append("<div class='info-box'>")
                .append("<div class='label'>Order Date:</div><div class='value'>").append(order.getCreatedAt()).append("</div>")
                .append("<div class='label'>Payment Status:</div><div class='value highlight'>").append(order.getPaymentStatus()).append("</div>")
                .append("</div>");

        html.append("<div class='info-box'>")
                .append("<div class='label'>Order Status:</div><div class='value highlight'>").append(order.getStatus()).append("</div>")
                .append("<div class='label'>Total Amount:</div><div class='value'>₹").append(String.format("%.2f", order.getTotalAmount())).append("</div>")
                .append("</div>");

        html.append("<div class='info-box'>")
                .append("<div class='label'>Payment Method:</div><div class='value'>").append(order.getPaymentMethod()).append("</div>")
                .append("<div class='label'>Shipping Address:</div><div class='value'>").append(order.getShippingAddress()).append("</div>")
                .append("</div>");

        html.append("</div>"); // End section

        html.append("</body></html>");

        return html.toString();
    }

    @GetMapping("/api/payment/success")
    public ResponseEntity<?> paymentSuccess(
            @RequestParam("paymentId") String paymentID,
            @RequestParam("PayerID") String payerID,
            @RequestParam("token") String paypalToken,
            @AuthenticationPrincipal UserDetails userDetails) throws PayPalRESTException, MessagingException, IOException {

        // 1. Execute PayPal payment
        Payment payment = paymentGateway.execute(paymentID, payerID);
        if (payment.getState().equals("approved")) {
            Users user = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Orders order = orderService.getLatestPendingOrderForUser(user);
            if (order != null) {
                // 3. Update order status
                order.setStatus(OrderStatus.CONFIRMED);
                order.setPaymentStatus(PaymentStatus.PAID);
                ordersRepository.save(order);

                String html = generateHtmlWithData(order,user);
                pdfService.createHtmlPDF(html);
                String path = "D:\\Harinathreddy\\My Projects\\Ecommerce\\Polimeras-Grocery\\src\\main\\resources\\uploads\\order.pdf";
                System.out.println("File created ✅");

                System.out.println("Mail is initiated in Online Payments");
                assert user != null;
                emailService.orderMailSend(user.getEmail(), user.getUsername(),order.getOrderNumber(),
                        order.getPaymentMethod(),order.getStatus(),order.getPaymentStatus(),order.getTotalAmount());
                System.out.println("Mail is sent✅");

                System.out.println(order.getStatus());
                System.out.println(order.getPaymentStatus());
                // 4. Build redirect URL with parameters
                URI redirectUri = UriComponentsBuilder.fromPath("/payment-success.html")
                        .queryParam("paymentId", paymentID)
                        .queryParam("PayerID", payerID)
                        .queryParam("name", user.getFirstname())
                        .queryParam("order_number", order.getOrderNumber())
                        .queryParam("total_amount", order.getTotalAmount())
                        .build()
                        .toUri();

                // 5. Return 302 redirect
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(redirectUri)
                        .build();
            }
        }

        // 6. Handle failed cases
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/payment-failed.html"))
                .build();
    }

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping("/api/payment/cancel")
    public String handlePaymentCancel(
            @RequestParam("token") String paypalToken,
            Model model,
            HttpServletRequest request) {

        // Log cancellation details
        log.warn("Payment cancelled - Token: {}, IP: {}",
                paypalToken, request.getRemoteAddr());

        // Add data to model
        model.addAttribute("token", paypalToken);
        model.addAttribute("cancellationTime", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));


        return "payment-cancel"; // Refers to payment-cancel.html
    }



}
