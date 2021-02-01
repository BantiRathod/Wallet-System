package com.banti.wallet.ums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banti.wallet.ums.model.Bank;
import com.banti.wallet.ums.repository.BankRepsitory;

@Service 
@Transactional
public class BankService {

	@Autowired
	private BankRepsitory bankRepo;
	
	public void saveBankDetail(Bank bank)
	{
		bankRepo.save(bank);
	}
	
	public Bank get(String accountNo){
		return bankRepo.findById(accountNo).get();
	}
}
