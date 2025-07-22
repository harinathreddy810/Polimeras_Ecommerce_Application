package com.Polimeras.Service;

import com.Polimeras.Entity.Cart;
import com.Polimeras.Entity.CartItems;
import com.Polimeras.Entity.Products;
import com.Polimeras.Entity.Users;
import com.Polimeras.Repository.CartItemsRepository;
import com.Polimeras.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private CatItemsService catItemsService;

    public Cart getOrCreateCart(Users user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart newCart = new Cart(user);
            newCart.setCartItems(new ArrayList<>());
            return cartRepository.save(newCart);
        }
    }

    public Cart addToCart(Users user, Products product, Integer quantity) {
        Cart cart = getOrCreateCart(user);

        // Check if product already exists in cart
        Optional<CartItems> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId()==(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItems item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item
            CartItems newItem = new CartItems(product,cart,product.getPrice(),quantity);
            cart.getCartItems().add(newItem);
        }

        updateCartTotal(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(Users user, Long productId) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().removeIf(item -> item.getProduct().getId()==(productId));
        updateCartTotal(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    public void clearCart(Users user) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().clear();
        cart.setTotalAmount(0);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .map(CartItems::getSubTotal)
                .reduce(0.0 , Double::sum);
        cart.setTotalAmount(total);
    }

}
