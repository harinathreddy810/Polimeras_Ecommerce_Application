package com.Polimeras.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.Polimeras.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @JsonManagedReference          // 👈 parent side
    @OneToMany(mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CartItems> cartItems = new ArrayList<>();

//    @OneToOne
//    @JoinColumn(name = "userId", nullable = false)
//    private Users user;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt=LocalDateTime.now();
//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<CartItems> cartItems;
    private double totalAmount = 0;

    public Cart() {
    }

    public Cart(Users user) {
        this.user = user;
    }

    public List<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}

//
//package com.Polimeras.Entity;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import com.Polimeras.DTO.UserDTO;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Cart {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;  // Changed from long to Long for better null handling
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private Users user;  // Using UserDTO as specified
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "cart",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY)
//    @ToString.Exclude
//    private List<CartItems> cartItems = new ArrayList<>();
//
//    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
//    private LocalDateTime updatedAt = LocalDateTime.now();
//
//    private BigDecimal totalAmount = BigDecimal.ZERO;  // Changed from double to BigDecimal for monetary values
//
//    // Removed duplicate constructors and getters/setters since @Data from Lombok handles them
//    // Removed commented-out code for clarity
//
//    // Custom constructor if needed
//    public Cart(UserDTO user) {
//        this.user = user;
//    }
//
//    // Helper method to add cart items
//    public void addCartItem(CartItems item) {
//        cartItems.add(item);
//        item.setCart(this);
//    }
//
//    // Helper method to remove cart items
//    public void removeCartItem(CartItems item) {
//        cartItems.remove(item);
//        item.setCart(null);
//    }
//}
