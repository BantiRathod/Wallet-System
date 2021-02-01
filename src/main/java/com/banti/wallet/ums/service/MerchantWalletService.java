package com.banti.wallet.ums.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.repository.MerchantWalletRepository;

@Service 
@Transactional
public class MerchantWalletService 
{

	@Autowired
	 private MerchantWalletRepository merchantWalletRepository;
	
	 public List< MerchantWallet> getAll()
	 {
		 return merchantWalletRepository.findAll();
	 }
	 
	 public  MerchantWallet get(String mobileNo)
	 {
	  return  merchantWalletRepository.findById(mobileNo).get();
	 }
	
	 public void create( MerchantWallet  merchantwallet)
	 {
		 merchantWalletRepository.save(merchantwallet);
	 }

	public void update(MerchantWallet payeeMerchantWallet) {
		merchantWalletRepository.save(payeeMerchantWallet);
		
	}	

}
