package com.Polimeras.Repository;

import com.Polimeras.Entity.DeliverySlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliverySlotsRepository extends JpaRepository<DeliverySlots,Long> {

}
