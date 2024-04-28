package com.todayeat.backend.seller.repository;

import com.todayeat.backend.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByEmail(String email);

    List<Seller> findByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);
}
