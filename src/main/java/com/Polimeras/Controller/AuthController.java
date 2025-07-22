package com.Polimeras.Controller;

import com.Polimeras.DTO.RegisterRequestDto;
import com.Polimeras.DTO.ResetPasswordDto;
import com.Polimeras.Entity.Users;
import com.Polimeras.Service.AuthService;
import com.Polimeras.Service.UserService;
//import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

//    HOME PAGE

    @GetMapping("/home")
    public ResponseEntity<Resource> homeController(){
        Resource page = new ClassPathResource("static/Home.html");
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(page);
    }

//    Registration

    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<?> registerHandler(@Valid @RequestBody RegisterRequestDto dto) {

        if (userService.existsByUsername(dto.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message",
                            "Username '" + dto.getUsername() + "' is already taken"));
        }

        if (userService.existsByEmail(dto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message",
                            "Email '" + dto.getEmail() + "' is already registered"));
        }

        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(dto.isActive());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setState(dto.getState());
        user.setZipcode(dto.getZipcode());
        user.setRole(dto.getRole());

        Users saved = userService.setNewUSer(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/login.html"))
                .body(saved);
    }


    @GetMapping("/register")
    @PermitAll
    public ResponseEntity<Void> showRegisterForm() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/register.html"))
                .build();
    }

//    Customized Login Page

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> login() {
        Resource page = new ClassPathResource("static/login.html");
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(page);
    }

//    Successful login dashboards

    @GetMapping("/dashboard")
    public ResponseEntity<Void> dashboard(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .location(URI.create("/login"))
                    .build();
        }

        return switch (user.getRole()) {
            case "ADMIN" -> ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("/api/admin/dashboard"))
                    .build();
            case "VENDOR" -> ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("/api/vendor/vendor-dashboard"))
                    .build();
            case "CUSTOMER" -> ResponseEntity
                    .status(HttpStatus.FOUND)
//                    .location(URI.create("/api/user/dashboard"))
                    .location(URI.create("/"))
                    .build();
            default -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .location(URI.create("/login"))
                    .build();
        };
    }

//  Password Rest

    @GetMapping(value = "/forgot-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> forgotPassword() {
        Resource page = new ClassPathResource("static/forgot-password.html");
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(page);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        authService.sendOtp(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        authService.sendOtp(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    @PostMapping("/reset-password-with-otp")
    public ResponseEntity<String> resetPasswordWithOtp(@Valid @RequestBody ResetPasswordDto dto) {
        authService.verifyOtpAndResetPassword(dto.getEmail(), dto.getOtp(), dto.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully.");
    }

}