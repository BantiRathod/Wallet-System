package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banti.wallet.ums.model.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant,Long>
{
 @Query("select m from Merchant m where m.mobileNo= :payeeMobileNo ")
 Merchant findByMobileNo(@Param("payeeMobileNo") String payeeMobileNo);
 	
}
