package com.banti.wallet.ums.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banti.wallet.ums.model.BaseWallet;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.repository.MerchantWalletRepository;

@Service (value="merchantWalletService")
@Transactional
public class MerchantWalletService implements MoneyMovementService
{
	Logger logger=LoggerFactory.getLogger(MerchantWalletService.class);
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

	public MerchantWallet update(MerchantWallet payeeMerchantWallet) {
		return merchantWalletRepository.save(payeeMerchantWallet);
		
	}

	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount) {
		MerchantWallet merchantWallet=(MerchantWallet) wallet;
		logger.info("received debit request of amount {} from merchant {}",amount,merchantWallet);
		merchantWallet.setBalance(merchantWallet.getBalance()-amount);
		MerchantWallet updatedMerchantWallet = update(merchantWallet);
		logger.info("merchant balance is {} after deducting amount{}",updatedMerchantWallet.getBalance(),amount);
		return updatedMerchantWallet;
	}

	@Override
	public BaseWallet creditMoney(BaseWallet wallet, double amount) {
		MerchantWallet merchantWallet=(MerchantWallet) wallet;
		logger.info("received credit request of amount {} from merchant {}",amount,merchantWallet);
		merchantWallet.setBalance(merchantWallet.getBalance()+amount);
		MerchantWallet updatedMerchantWallet = update(merchantWallet);
		logger.info("merchant balance is {} after crediting amount{}",updatedMerchantWallet.getBalance(),amount);
		return updatedMerchantWallet;
	}	

}
