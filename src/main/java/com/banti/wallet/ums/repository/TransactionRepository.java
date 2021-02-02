package com.banti.wallet.ums.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import org.springframework.data.repository.PagingAndSortingRepository;
import com.banti.wallet.ums.model.WalletTransaction;

public interface TransactionRepository extends JpaRepository<WalletTransaction,Long> {
	
	
	  @Query("Select t from WalletTransaction t where t.payerMobileNo = :payerNo ")                                  //jpa query for retrieving tran. sum for given mobileno                                                                                                                                                                                             // 
	  Page<WalletTransaction> findAllByPayerNo(@Param("payerNo") String payerNo, Pageable pageable);	 
}
