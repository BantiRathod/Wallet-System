package com.banti.wallet.ums.service;

import java.util.Date;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;
import com.banti.wallet.ums.elasticsearch.repositories.ElasticPersonWalletRepository;
import com.banti.wallet.ums.enums.AccountStatus;
import com.banti.wallet.ums.model.BaseWallet;
//import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.repository.PersonWalletRepository;
import com.banti.wallet.ums.requestEntities.PersonWalletRequest;
//import com.banti.wallet.ums.requestEntities.UpdatePersonWalletRequest;
//import com.banti.wallet.ums.validator.business.PersonWalletBusinessValidator;

@Service(value="personWalletService")
@Transactional
public class PersonWalletService implements MoneyMovementService { 
	Logger logger=LoggerFactory.getLogger(PersonWalletService.class);
	
//	@Autowired
//	private PersonWalletBusinessValidator personWalletBusinessValidator;
	
	@Autowired
	private ElasticPersonWalletRepository elasticPersonWalletRepository;
	
	@Autowired
	private PersonWalletRepository personWalletRepository;
	
	
	 public Iterable<ElasticPersonWallet> getAllPersonWallet()
	 {
		 return elasticPersonWalletRepository.findAll();
	 }
	 

		public PersonWallet getPersonWalletFromMysql(String mobileNo) {
			return 	personWalletRepository.findById(mobileNo).get();
			
		}
	 public ElasticPersonWallet getPersonWallet(String mobileNo)
	 {
	  return  elasticPersonWalletRepository.findById(mobileNo).get();
	 }
	
	 
	 
	 public void createPersonWallet(PersonWalletRequest wallet)
	 {
		 PersonWallet personWallet = new PersonWallet();
		 
		 personWallet.setMobileNo(wallet.getMobileNo());
		 personWallet.setBalance(wallet.getBalance());
		 personWallet.setCreatedDate(new Date());
		 personWallet.setStatus(AccountStatus.ENABLED.name());
		 // TO STORE IN MYSQL
		 personWalletRepository.save(personWallet);
		 
		 ElasticPersonWallet elasticPersonWallet =new ElasticPersonWallet();
		 elasticPersonWallet.setMobileNo(wallet.getMobileNo());
		 elasticPersonWallet.setBalance(wallet.getBalance());
		 elasticPersonWallet.setCreatedDate(new Date());
		 elasticPersonWallet.setStatus(AccountStatus.ENABLED.name());
		 //TO STORE IN ELASTISEARCH
		 elasticPersonWalletRepository.save(elasticPersonWallet);
	 }	
	 
	public void deletePersonWallet(String mobileNo)
	{
		personWalletRepository.deleteById(mobileNo);
		elasticPersonWalletRepository.deleteById(mobileNo);	
	}
	
	
	
	
// rename user wallet to person wallet
	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount){
		PersonWallet personWallet=(PersonWallet) wallet;
		
		logger.info("received debit request of amount {} from user wallet {}",amount,personWallet);
		personWallet.setBalance(personWallet.getBalance()-amount);
		
		// KNOW IN DETAIL ABOUT IT BY MERCHANT WALLET UPDATE TIME
		PersonWallet  newPersonWallet = new PersonWallet(personWallet.getMobileNo(),personWallet.getBalance()
				,personWallet.getStatus(),personWallet.getCreatedDate());
		
		PersonWallet updatedWallet=personWalletRepository.save(newPersonWallet);
		
		logger.info("merchant balance is {} after deducting amount{}",personWallet.getBalance(),amount);
		return updatedWallet;
	}

	@Override
	public BaseWallet creditMoney(BaseWallet wallet, double amount) {
		PersonWallet personWallet=(PersonWallet) wallet;
		
		logger.info("received credit request of amount {} from user wallet {}", amount , personWallet);
		
		personWallet.setBalance(personWallet.getBalance()+amount);
		
		PersonWallet  newPersonWallet = new PersonWallet(personWallet.getMobileNo(),personWallet.getBalance()
				,personWallet.getStatus(),personWallet.getCreatedDate());
		
		PersonWallet updatedWallet=personWalletRepository.save(newPersonWallet);
		
		logger.info("person balance is {} after credit amount{}",personWallet.getBalance(),amount);
		return updatedWallet;
	}

}

/*
 * public void updatePersonWallet(UpdatePersonWalletRequest wallet, String
 * mobileNo) throws Exception {
 * personWalletBusinessValidator.updatePersonWalletValidatoion(wallet,mobileNo);
 * 
 * PersonWallet personWallet = personWalletRepository.findById(mobileNo).get();
 * 
 * PersonWallet newPersonWallet = new PersonWallet(wallet.getMobileNo(),
 * personWallet.getBalance(), personWallet.getStatus(),
 * personWallet.getCreatedDate());
 * 
 * //SAVE NEW PRESON WALLET OBJECT personWalletRepository.save(newPersonWallet);
 * 
 * ElasticPersonWallet elasticPersonWallet =
 * elasticPersonWalletRepository.findById(mobileNo).get();
 * 
 * ElasticPersonWallet newElasticPersonWallet = new
 * ElasticPersonWallet(wallet.getMobileNo(), elasticPersonWallet.getBalance(),
 * elasticPersonWallet.getStatus(), elasticPersonWallet.getCreatedDate());
 * //SAVE NEW PRESON WALLET OBJECT
 * elasticPersonWalletRepository.save(newElasticPersonWallet); }
 */