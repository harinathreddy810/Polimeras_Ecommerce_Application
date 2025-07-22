// … imports unchanged …
package com.Polimeras.Controller;

import com.Polimeras.DTO.CartItemDTO;
import com.Polimeras.DTO.CheckoutRequest;
import com.Polimeras.DTO.ProductDTO;
import com.Polimeras.Entity.*;
import com.Polimeras.Repository.*;
import com.Polimeras.Service.*;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.Polimeras.Service.ProductService;
import com.Polimeras.Service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('CUSTOMER')")
public class UserController {

    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService  emailService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private AuthService    authService;
    @Autowired
    private CartService    cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private OrdersRepository ordersRepository;

//    1. PROFILE
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        Optional<Users> user = userService.getUserDetails(principal);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

//    2. DASHBOARD

    @GetMapping("/dashboard")
    public ResponseEntity<Resource> dashboard() {
        Resource page = new ClassPathResource("/static/user-dashboard.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(page);
    }

    @GetMapping("/dashboard/info")
    public ResponseEntity<?> getDashboardInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

//    3. CART & CHECKOUT

    // Add to cart
    @PostMapping("/add-to-cart/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId,
                                       @RequestParam(defaultValue = "1") Integer quantity,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        Products product = productService.getProductById(productId).orElse(null);

        if (user == null)     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        if (product == null)  return ResponseEntity.badRequest().body("Product not found");
        if (product.getStockQuantity() < quantity)
            return ResponseEntity.badRequest().body("Insufficient stock");

        Cart cart = cartService.addToCart(user, product, quantity);
        System.out.println(cart);
        return ResponseEntity.ok("Product added to cart");
    }

    // Get cart
    @GetMapping("/cart")
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

        Cart cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(cart);
    }

    // Remove item
    @DeleteMapping("/cart/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

        cartService.removeFromCart(user, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    // Checkout summary
    @GetMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

        Cart cart = cartService.getOrCreateCart(user);
        if (cart.getCartItems().isEmpty()) return ResponseEntity.badRequest().body("Cart is empty");

        return ResponseEntity.ok(Map.of("user", user, "cart", cart));
    }

    // Process checkout
//    @PostMapping("/checkout/process")
//    public ResponseEntity<?> processCheckout(@RequestParam PaymentMethod paymentMethod,
//                                  @RequestParam String address,
//                                  @AuthenticationPrincipal UserDetails userDetails) {
//
//        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);
//
//        try {
//            Orders order = orderService.createOrder(user, paymentMethod, address);
//
//            if ("ONLINE"==(PaymentMethod.ONLINE.name())) {
//                return ResponseEntity.status(200).location(URI.create("/api/payment/**")).build();
//            }
//
////            ("success",
////                    "Order #" + order.getOrderNumber() + " placed successfully!");
//            return ResponseEntity.status(200).location(URI.create("/api/user/orders")).body(
//                    "success" + "Order #" + order.getOrderNumber() +"Order Placed Successfully!"
//            );
//
//        } catch (IllegalStateException e) {
//            return ResponseEntity.ok().body(e.getMessage());
//        }
//    }

//    @PostMapping("/checkout/process")
//    public ResponseEntity<?> processCheckout(
//            @ModelAttribute CheckoutRequest checkoutRequest,
//            @AuthenticationPrincipal UserDetails userDetails
//    ) {
//        // ...
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


    @PostMapping("/checkout/process")
    public ResponseEntity<?> processCheckout(@RequestParam String paymentMethod,
                                             @RequestParam String address,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);

        try {
//            Orders order = orderService.createOrder(user, paymentMethod,address);

            if ("ONLINE".equalsIgnoreCase(paymentMethod)) {
                Orders order = orderService.createOrder(user, paymentMethod,address,
                        OrderStatus.PENDING,PaymentStatus.PENDING);
                return ResponseEntity.status(200).location(URI.create("/api/payment/pay")).build();
            }else if ("COD".equalsIgnoreCase(paymentMethod)){
                Orders order = orderService.createOrder(user, paymentMethod,address,
                        OrderStatus.CONFIRMED,PaymentStatus.PENDING);
                String html = generateHtmlWithData(order,user);
                pdfService.createHtmlPDF(html);
                String path = "D:\\Harinathreddy\\My Projects\\Ecommerce\\Polimeras-Grocery\\src\\main\\resources\\uploads\\order.pdf";
                System.out.println("File created ✅");

                System.out.println("Mail is initiated in COD");
                assert user != null;
                emailService.orderMailSend(user.getEmail(), user.getUsername(),order.getOrderNumber(),
                        order.getPaymentMethod(),order.getStatus(),order.getPaymentStatus(),order.getTotalAmount());
                System.out.println("Mail is sent✅");

                return ResponseEntity.status(200).location(URI.create("/api/user/orders")).body(
                        "Order Placed Successfully! "+ "This is your Order ID : " + order.getOrderNumber()
                );
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.ok().body(e.getMessage());
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/cart/decrease/{productId}")
    public ResponseEntity<?> decreaseCartItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Users> optionalUser = userService.findByUsername(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Users user = optionalUser.get();
        Cart cart = cartService.getOrCreateCart(user);

        Optional<CartItems> optionalItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId()==(productId))
                .findFirst();

        if (optionalItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart");
        }

        CartItems item = optionalItem.get();

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            item.setSubTotal(item.getQuantity() * item.getPrice());
            cartItemsRepository.save(item);
        } else {
            cart.getCartItems().remove(item);
            cartItemsRepository.delete(item);
        }

        cart.setTotalAmount(cart.getCartItems().stream()
                .mapToDouble(CartItems::getSubTotal)
                .sum());

        cartRepository.save(cart); // ✅ Save the cart correctly

        return ResponseEntity.ok("Item quantity decreased");
    }



}
