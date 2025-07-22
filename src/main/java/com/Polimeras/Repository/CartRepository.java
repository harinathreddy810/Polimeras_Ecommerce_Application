package com.Polimeras.Repository;

import com.Polimeras.Entity.Cart;
import com.Polimeras.Entity.CartItems;
import com.Polimeras.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUser(Users user);

}
