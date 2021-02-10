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

@Service (value="merchantWalletService")
@Transactional
public class MerchantWalletService implements MoneyMovementService
{
	Logger logger=LoggerFactory.getLogger(MerchantWalletService.class);
	 @Autowired
	 private ElasticMerchantWalletRepository  elasticMerchantWalletRepository;
	 @Autowired
	 private MerchantWalletRepository merchantWalletRepository;
	
	 public Iterable<ElasticMerchantWallet> getListOfAllMerchantWallet()
	 {
		 return  elasticMerchantWalletRepository.findAll();
	 }
	 
	 public  ElasticMerchantWallet getMerchantWallet(String mobileNo)
	 {
	  return   elasticMerchantWalletRepository.findById(mobileNo).get();
	 }
	
	 public void createMerchantWallet(MerchantWalletRequest  merchantWallet) throws Exception
	 {
		 //TO CHECK WHETHER ACCOUNT IS ALREADY EXIST OR NOT
		 ElasticMerchantWallet tempMerchantWallet = elasticMerchantWalletRepository.findById(merchantWallet.getMobileNo()).get();
		 if(tempMerchantWallet!=null)
			 throw new Exception("merchant account is already oppened with this mobile number "+ merchantWallet.getMobileNo());
			 
		 if(merchantWallet.getMobileNo().length()!=10)
			  throw new Exception("Invalid mobile No entered "+ merchantWallet.getMobileNo());
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
		ElasticMerchantWallet elasticMerchantWallet = elasticMerchantWalletRepository.findById(mobileNo).get();
		 if(elasticMerchantWallet==null)
		    	 throw new Exception("account not opened yet by this mobile number");
		 
		 if(mobileNo.length()!=10)
			 throw new Exception("Invalid number passed, mobile no="+mobileNo);
		 
		 elasticMerchantWallet.setMobileNo(mobileNo);
		 elasticMerchantWalletRepository.save( elasticMerchantWallet);
		 
		 MerchantWallet tempMerchantWallet = merchantWalletRepository.findById(mobileNo).get();
		 tempMerchantWallet.setMobileNo(mobileNo);
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
