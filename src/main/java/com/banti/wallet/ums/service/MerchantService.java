package com.banti.wallet.ums.service;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;
import com.banti.wallet.ums.elasticsearch.repositories.ElasticMerchantRepository;
import com.banti.wallet.ums.enums.PersonStatus;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.repository.MerchantRepository;

@Service
@Transactional
public class MerchantService
{
  @Autowired
  private PasswordEncoder bcrptEncoder;
  @Autowired
  private MerchantRepository merchantRepository;
  @Autowired
  private ElasticMerchantRepository elasticMerchantRepository;
 
   public Iterable<ElasticMerchant> getListOfAllMerchants()
	 {
		 return  elasticMerchantRepository.findAll();
	 }
	 
	 public ElasticMerchant getMerchant(Long id)
	 {
	  return elasticMerchantRepository.findById(id).get();
	 }
	

	public ElasticMerchant findByMobileNo(String payeeMobileNo) {
			return elasticMerchantRepository.findByMobileNo(payeeMobileNo);
	}	
		
	 public void createMerchantAccount(Merchant merchant)
	 {
		 Merchant tempMerchant = new Merchant();
		 tempMerchant.setMobileNo(merchant.getMobileNo());
		 tempMerchant.setAddress(merchant.getAddress());
		 tempMerchant.setEmail(merchant.getEmail());
		 tempMerchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 tempMerchant.setRegisterDate(new Date());
		 tempMerchant.setStatus(PersonStatus.ACTIVE.name());
		 // MERCHANT RECORD HAS BEEN STORED IN MYSQL
		 tempMerchant = merchantRepository.save(merchant);
		 
		 ElasticMerchant elasticMerchant = new ElasticMerchant();
		 elasticMerchant.setMobileNo(merchant.getMobileNo());
		 elasticMerchant.setAddress(merchant.getAddress());
		 elasticMerchant.setEmail(merchant.getEmail());
		 elasticMerchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 elasticMerchant.setRegisterDate(new Date());
		 elasticMerchant.setStatus(PersonStatus.ACTIVE.name());
		 elasticMerchant.setMerchantId(tempMerchant.getMerchantId());
		 // MERCHANT RECORD HAS BEEN STORED IN ELASTICSEARCH DATABASE
		 elasticMerchantRepository.save(elasticMerchant);
	 
	 }

	 public void updateMerchantAccount(Merchant merchant) throws NoSuchElementException
	 {
		 ElasticMerchant elasticMerchant = elasticMerchantRepository.findById(merchant.getMerchantId()).get();
		 
		 if(elasticMerchant==null)
			  throw new NoSuchElementException("mrchant is exist of this id="+ merchant.getMerchantId());
		 
		 elasticMerchant.setAddress(merchant.getAddress());
		 elasticMerchant.setEmail(merchant.getEmail());
		 elasticMerchant.setMobileNo(merchant.getMobileNo());
		 elasticMerchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 elasticMerchant.setShopName(merchant.getShopName());
		 
		 elasticMerchantRepository.save(elasticMerchant);
		 
		 merchant.setAddress(merchant.getAddress());
		 merchant.setEmail(merchant.getEmail());
		 merchant.setMobileNo(merchant.getMobileNo());
		 merchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 merchant.setShopName(merchant.getShopName());
		 
		 merchantRepository.save(merchant);
		 
		 
		 
	 }
	 public void deleteMerchantAccount(Long id)
	 {
		 merchantRepository.deleteById(id);
		 elasticMerchantRepository.deleteById(id);		 
	 }
   
}
