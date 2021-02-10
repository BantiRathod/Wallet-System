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

import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.repository.PersonWalletRepository;
import com.banti.wallet.ums.requestEntities.PersonWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdatePersonWalletRequest;

@Service(value="personWalletService")
@Transactional
public class PersonWalletService implements MoneyMovementService { 
	Logger logger=LoggerFactory.getLogger(PersonWalletService.class);
	
	@Autowired
	private ElasticPersonWalletRepository elasticPersonWalletRepository;
	
	@Autowired
	private PersonWalletRepository personWalletRepository;
	
	
	 public Iterable<ElasticPersonWallet> getAllPersonWallet()
	 {
		 return elasticPersonWalletRepository.findAll();
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
	
	
	public void updatePersonWallet(UpdatePersonWalletRequest wallet, String mobileNo) throws Exception
	{
		 ElasticPersonWallet elasticPersonWallet  = elasticPersonWalletRepository.findById(mobileNo).get();
		 if(elasticPersonWallet==null)
			 throw new Exception("NO PERSON WALLET EXIST OF THIS NUMBER ");
		 
		
		 elasticPersonWallet.setMobileNo(wallet.getMobileNo());
		 elasticPersonWalletRepository.save(elasticPersonWallet);
		  
		 PersonWallet personWallet = personWalletRepository.findById(mobileNo).get();
		 personWallet.setMobileNo(wallet.getMobileNo());
		 personWalletRepository.save(personWallet);
	
	}
	
// rename user wallet to person wallet
	@Override
	public BaseWallet debitMoney(BaseWallet wallet, double amount){
		PersonWallet personWallet=(PersonWallet) wallet;
		
		logger.info("received debit request of amount {} from user wallet {}",amount,personWallet);
		personWallet.setBalance(personWallet.getBalance()-amount);
		
		PersonWallet updatedWallet=personWalletRepository.save(personWallet);
		
		logger.info("merchant balance is {} after deducting amount{}",personWallet.getBalance(),amount);
		return updatedWallet;
	}

	@Override
	public BaseWallet creditMoney(BaseWallet wallet, double amount) {
		PersonWallet personWallet=(PersonWallet) wallet;
		
		logger.info("received credit request of amount {} from user wallet {}", amount , personWallet);
		
		personWallet.setBalance(personWallet.getBalance()+amount);
		
		PersonWallet updatedWallet=personWalletRepository.save(personWallet);
		
		logger.info("person balance is {} after credit amount{}",personWallet.getBalance(),amount);
		return updatedWallet;
	}

}
