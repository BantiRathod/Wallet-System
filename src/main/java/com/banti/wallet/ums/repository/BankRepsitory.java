package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banti.wallet.ums.model.Bank;

public interface BankRepsitory extends JpaRepository<Bank,String>{
	
}
