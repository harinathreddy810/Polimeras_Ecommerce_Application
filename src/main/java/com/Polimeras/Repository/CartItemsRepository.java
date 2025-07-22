package com.Polimeras.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<com.Polimeras.Entity.CartItems,Long> {

}
