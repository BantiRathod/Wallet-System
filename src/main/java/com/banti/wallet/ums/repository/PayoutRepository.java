package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banti.wallet.ums.scheduling.Payout;

public interface PayoutRepository extends JpaRepository<Payout,Long>{
   
}
