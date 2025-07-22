package com.Polimeras.Repository;

import com.Polimeras.Entity.OrderStatus;
import com.Polimeras.Entity.Orders;

import com.Polimeras.Entity.Users;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {

    List<Orders> findByUser(Users user);
    List<Orders> findByOrderByCreatedAtDesc();
    List<Orders> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Orders o JOIN o.orderItems oi WHERE oi.product.user = :vendor")
    List<Orders> findByVendor(@Param("vendor") Users vendor);


    Orders findTopByUserAndStatusOrderByCreatedAtDesc(Users user, OrderStatus status);
    
    //newly added
	List<Orders> findByOrderItems_Product_User(Users vendor);
}
