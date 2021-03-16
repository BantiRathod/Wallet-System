package com.banti.wallet.ums.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


//import org.springframework.data.repository.PagingAndSortingRepository;
import com.banti.wallet.ums.model.WalletTransaction;

public interface TransactionRepository extends JpaRepository<WalletTransaction,Long> {
	
	
	  @Query("Select t from WalletTransaction t where t.payerMobileNo = :payerNo ")                                                                                                                                                                                                                    // 
	  Page<WalletTransaction> findAllByPayerNo(@Param("payerNo") String payerNo, Pageable pageable);

	  @Query("Select t from WalletTransaction t where (t.payeeMobileNo= :mobileNo) and (t.transactionDate between :startDate and :endDate)")
	  List<WalletTransaction> getMerchantTransaction(@Param("mobileNo") String mobileNo, @Param("startDate") Date startDate, @Param("endDate") Date endDate);	 
}
