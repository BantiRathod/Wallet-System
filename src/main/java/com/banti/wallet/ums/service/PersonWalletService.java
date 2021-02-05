package com.banti.wallet.ums.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.BaseWallet;

import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.repository.PersonWalletRepository;

@Service(value="userWalletService")
@Transactional
public class PersonWalletService implements MoneyMovementService { 
	Logger logger=LoggerFactory.getLogger(PersonWalletService.class);
	@Autowired
	private PersonWalletRepository wrepo;
	
	 public List<PersonWallet> getAll()
	 {
		 return wrepo.findAll();
	 }
	 
	 public PersonWallet get(String mobileNo)
	 {
	  return  wrepo.findById(mobileNo).get();
	 }
	
	 public void create(PersonWallet wallet)
	 {
			wrepo.save(wallet);
	 }	
	 
	public void update(PersonWallet wallet)
	{
		wrepo.save(wallet);
	}
	
// rename user wallet to person wallet
	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount) {
		PersonWallet userWallet=(PersonWallet) wallet;
		logger.info("received debit request of amount {} from user wallet {}",amount,userWallet);
		userWallet.setBalance(userWallet.getBalance()-amount);
		update(userWallet);
		logger.info("merchant balance is {} after deducting amount{}",userWallet.getBalance(),amount);
		return userWallet;
	}

	@Override
	public BaseWallet creditMoney(BaseWallet wallet, double amount) {
		PersonWallet userWallet=(PersonWallet) wallet;
		logger.info("received credit request of amount {} from user wallet {}",amount,userWallet);
		userWallet.setBalance(userWallet.getBalance()+amount);
		update(userWallet);
		logger.info("user balance is {} after credit amount{}",userWallet.getBalance(),amount);
		return userWallet;
	}	

}
