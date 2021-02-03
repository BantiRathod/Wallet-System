package com.banti.wallet.ums.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.BaseWallet;

import com.banti.wallet.ums.model.Wallet;
import com.banti.wallet.ums.repository.WalletRepository;

@Service(value="userWalletService")
@Transactional
public class UserWalletService implements MoneyMovementService { 
	Logger logger=LoggerFactory.getLogger(UserWalletService.class);
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
	

	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount) {
		Wallet userWallet=(Wallet) wallet;
		logger.info("received debit request of amount {} from user wallet {}",amount,userWallet);
		userWallet.setBalance(userWallet.getBalance()-amount);
		update(userWallet);
		logger.info("merchant balance is {} after deducting amount{}",userWallet.getBalance(),amount);
		return userWallet;
	}

	@Override
	public BaseWallet creditMoney(BaseWallet wallet, double amount) {
		Wallet userWallet=(Wallet) wallet;
		logger.info("received credit request of amount {} from user wallet {}",amount,userWallet);
		userWallet.setBalance(userWallet.getBalance()+amount);
		update(userWallet);
		logger.info("merchant balance is {} after credit amount{}",userWallet.getBalance(),amount);
		return userWallet;
	}	

}
