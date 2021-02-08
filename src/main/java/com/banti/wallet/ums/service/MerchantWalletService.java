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
	 
	 public  ElasticMerchantWallet get(String mobileNo)
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
		 
		 
		 ElasticMerchantWallet  elasticMerchantWallet = new  ElasticMerchantWallet();
		 elasticMerchantWallet.setBalance(merchantWallet.getBalance());
		 elasticMerchantWallet.setMobileNo(merchantWallet.getMobileNo());
		 elasticMerchantWallet.setMerchantWalletcreatedDate(new Date());
		 elasticMerchantWallet.setStatus(AccountStatus.ENABLED.name());
		 elasticMerchantWalletRepository.save(elasticMerchantWallet);
		 
		 MerchantWallet  tempmerchantWallet = new  MerchantWallet();
		 tempmerchantWallet.setBalance(merchantWallet.getBalance());
		 tempmerchantWallet.setMerchantWalletcreatedDate(elasticMerchantWallet.getMerchantWalletcreatedDate());
		 tempmerchantWallet.setMobileNo(merchantWallet.getMobileNo());
		 tempmerchantWallet.setStatus(AccountStatus.ENABLED.name());
		 
		 merchantWalletRepository.save(tempmerchantWallet);
		 
	 }

	public MerchantWallet update(MerchantWallet payeeMerchantWallet) {
		return merchantWalletRepository.save(payeeMerchantWallet);
		
	}

	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount) {     // What we did here
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
