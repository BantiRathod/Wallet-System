package com.banti.wallet.ums.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banti.wallet.ums.model.Wallet;
import com.banti.wallet.ums.repository.WalletRepository;

@Service
@Transactional
public class WalletService { 
	@Autowired
	private WalletRepository wrepo;
	
	 public List<Wallet> getAll()
	 {
		 return wrepo.findAll();
	 }
	 
	 public Wallet get(String mobileNo)
	 {
	  return  wrepo.findById(mobileNo).get();
	 }
	
	 public void create(Wallet wallet)
	 {
			wrepo.save(wallet);
	 }	
	 
	public void update(Wallet wallet)
	{
		wrepo.save(wallet);
	}	
}
