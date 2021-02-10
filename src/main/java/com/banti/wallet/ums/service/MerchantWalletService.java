package com.banti.wallet.ums.service;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchantWallet;
import com.banti.wallet.ums.elasticsearch.repositories.ElasticMerchantWalletRepository;
import com.banti.wallet.ums.enums.AccountStatus;
import com.banti.wallet.ums.model.BaseWallet;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.repository.MerchantWalletRepository;
import com.banti.wallet.ums.requestEntities.MerchantWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantWalletRequest;
import com.banti.wallet.ums.validator.business.MerchantWalletBusinessValidator;

@Service (value="merchantWalletService")
@Transactional
public class MerchantWalletService implements MoneyMovementService
{
	Logger logger=LoggerFactory.getLogger(MerchantWalletService.class);
	
	 @Autowired
	 private ElasticMerchantWalletRepository  elasticMerchantWalletRepository;
	 
	 @Autowired
	 private MerchantWalletRepository merchantWalletRepository;
	 
	 @Autowired
	 private MerchantWalletBusinessValidator merchantWalletBusinessValidator;
	 
	 
	 public Iterable<ElasticMerchantWallet> getListOfAllMerchantWallet()
	 {
		 return  elasticMerchantWalletRepository.findAll();
	 }
	 
	 
	 public  ElasticMerchantWallet getMerchantWallet(String mobileNo)
	 {
		 
	    return   elasticMerchantWalletRepository.findById(mobileNo).get();
	 }
	
	 
	public void deleteMerchantWallet(String mobileNo) {
			 merchantWalletRepository.deleteById(mobileNo);
			 elasticMerchantWalletRepository.deleteById(mobileNo);
	 }	
	
	
	 public void createMerchantWallet(MerchantWalletRequest  merchantWallet) throws Exception
	 {
		 //TO BUSINESS VALIDATION FOR CREATE Merchant Wallet 
		 merchantWalletBusinessValidator.createMerchantWalletValidation(merchantWallet);
		 
		 MerchantWallet  tempmerchantWallet = new  MerchantWallet();
		 
		 tempmerchantWallet.setBalance(merchantWallet.getBalance());
		 tempmerchantWallet.setMerchantWalletcreatedDate(new Date());
		 tempmerchantWallet.setMobileNo(merchantWallet.getMobileNo());
		 tempmerchantWallet.setStatus(AccountStatus.ENABLED.name());
		 merchantWalletRepository.save(tempmerchantWallet);
		 
		 ElasticMerchantWallet  elasticMerchantWallet = new  ElasticMerchantWallet();
		 elasticMerchantWallet.setBalance(merchantWallet.getBalance());
		 elasticMerchantWallet.setMobileNo(merchantWallet.getMobileNo());
		 elasticMerchantWallet.setMerchantWalletcreatedDate( tempmerchantWallet.getMerchantWalletcreatedDate());
		 elasticMerchantWallet.setStatus(AccountStatus.ENABLED.name());
		 elasticMerchantWalletRepository.save(elasticMerchantWallet);
		 	 
	 }

	public void updateMerchantWallet(UpdateMerchantWalletRequest merchantWallet, String mobileNo) throws Exception 
	{
		//FOR BUSINESS VALIADATION
		 merchantWalletBusinessValidator.updateMerchantWalletValidation(merchantWallet,mobileNo);
		
		 ElasticMerchantWallet elasticMerchantWallet = elasticMerchantWalletRepository.findById(mobileNo).get();
		 elasticMerchantWallet.setMobileNo(mobileNo);
		 elasticMerchantWalletRepository.save( elasticMerchantWallet);
		 
		 MerchantWallet tempMerchantWallet = merchantWalletRepository.findById(mobileNo).get();
		 tempMerchantWallet.setMobileNo(merchantWallet.getMobileNo());
		 merchantWalletRepository.save(tempMerchantWallet);
		
	}

	
	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount) {     // What we did here
		MerchantWallet merchantWallet=(MerchantWallet) wallet;
		logger.info("received debit request of amount {} from merchant {}",amount,merchantWallet);
		merchantWallet.setBalance(merchantWallet.getBalance()-amount);
		MerchantWallet updatedMerchantWallet=merchantWalletRepository.save(merchantWallet);
		logger.info("merchant balance is {} after deducting amount{}", updatedMerchantWallet.getBalance(),amount);
		
		return  updatedMerchantWallet;
	}

	@Override
	public BaseWallet creditMoney(BaseWallet wallet, double amount) {
		MerchantWallet merchantWallet=(MerchantWallet) wallet;
		logger.info("received credit request of amount {} from merchant {}",amount,merchantWallet);
		 merchantWallet.setBalance(merchantWallet.getBalance()+amount);
		 MerchantWallet updatedMerchantWallet=merchantWalletRepository.save(merchantWallet);
		 logger.info("merchant balance is {} after crediting amount {}"+ updatedMerchantWallet.getBalance());
		return  updatedMerchantWallet;
	}


}
