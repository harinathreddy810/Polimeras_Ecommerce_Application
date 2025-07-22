package com.Polimeras.Controller;

import com.Polimeras.Entity.Users;
import com.Polimeras.Service.EmailService;
import com.Polimeras.Service.ProductService;
import com.Polimeras.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private UserService    userService;
    @Autowired private ProductService productService;
    @Autowired private EmailService   mailService;

    /* ---------- dashboard JSON ---------- */
    @GetMapping("/dashboard-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,Object>> dashboardData() {
        Map<String,Object> body = new HashMap<>();
        List<Users> all = userService.findAllUsers();

        body.put("totalUsers",     all.size());
        body.put("totalCustomers", userService.findByRole("CUSTOMER").size());
        body.put("totalVendors",   userService.findByRole("VENDOR").size());
        body.put("totalProducts",  productService.getAllProducts().size());
        body.put("users",          all);

        return ResponseEntity.ok(body);
    }

    /* ---------- optional redirect ---------- */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> dashboardPage() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/dashboard.html"))
                .build();
    }

    /* ---------- enable / disable toggle ---------- */
    @PostMapping("/user/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) {
        Users updated;
        try {
            updated = userService.toggleStatus(id);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You cannot disable your own account");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(Map.of(
                "id",      updated.getId(),
                "enabled", updated.isEnabled()));
    }


    /* ---------- delete ---------- */
    @PostMapping("/user/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try { userService.deleteUser(id); return ResponseEntity.ok("User deleted"); }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user");
        }
    }

    /* ---------- VERY SIMPLE block / approve ---------- */

    @PostMapping("/block/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockUser(@PathVariable String username) {
        Users user = userService.findByUsername(username)
                .orElse(null);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

//        mailService.sendBlockNotification(user.getEmail(), user.getUsername());
        userService.blockUser(user);                  // sets role=BLOCKED + disabled

        return ResponseEntity.ok("User blocked");
    }

    @PostMapping("/approve/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveUser(@PathVariable String username) {
        Users user = userService.findByUsername(username)
                .orElse(null);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        userService.approveUser(user);                // sets role=ROLE_USER + enabled
//        mailService.sendApprovalNotification(user.getEmail(), user.getUsername());
//        System.out.println(user.getEmail());
        return ResponseEntity.ok("User approved");
    }
}
