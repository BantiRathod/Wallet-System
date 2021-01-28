package com.banti.wallet.ums.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.PagingAndSortingRepository;
import com.banti.wallet.ums.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
	@Query("Select * from transaction where payerNo like %?1%")
	Page<Transaction> findAllByPayerNo(String payerNo,Pageable pageable);
}
