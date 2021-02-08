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
import com.banti.wallet.ums.requestEntities.MerchantRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantRequest;

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
	 
	 public ElasticMerchant getMerchant(Long id) throws Exception
	 {
	  if(id<0)
		  throw new Exception("Invalid or negative id entered or negative , "+id);
	  return elasticMerchantRepository.findById(id).get();
	 }
	

	public ElasticMerchant findByMobileNo(String payeeMobileNo) {
			return elasticMerchantRepository.findByMobileNo(payeeMobileNo);
	}	
		
	 public void createMerchantAccount(MerchantRequest merchant) throws Exception
	 {
		
		 if(elasticMerchantRepository.findByMobileNo(merchant.getMobileNo())!=null)
			  throw new Exception("Merchant account is already exist with this mobile number"+merchant.getMobileNo());
		 
		 Merchant tempMerchant = new Merchant();
		 tempMerchant.setMobileNo(merchant.getMobileNo());
		 tempMerchant.setAddress(merchant.getAddress());
		 tempMerchant.setEmail(merchant.getEmail());
		 tempMerchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 tempMerchant.setRegisterDate(new Date());
		 tempMerchant.setStatus(PersonStatus.ACTIVE.name());
		 // MERCHANT RECORD HAS BEEN STORED IN MYSQL
		Merchant returnMerchant = merchantRepository.save(tempMerchant);
		
		 
		ElasticMerchant elasticMerchant = new ElasticMerchant();
		 elasticMerchant.setAddress(merchant.getAddress());
		 elasticMerchant.setEmail(merchant.getEmail());
		 elasticMerchant.setMobileNo(merchant.getMobileNo());
		 elasticMerchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 elasticMerchant.setShopName(merchant.getShopName());
		 elasticMerchant.setStatus(PersonStatus.ACTIVE.name());
		 elasticMerchant.setMerchantId( returnMerchant.getMerchantId());
		 // MERCHANT RECORD HAS BEEN STORED IN ELASTICSEARCH DATABASE
		 elasticMerchantRepository.save(elasticMerchant);
	 
	 }

	 public void updateMerchantAccount(UpdateMerchantRequest merchant, Long id) throws NoSuchElementException
	 {
		 ElasticMerchant elasticMerchant = elasticMerchantRepository.findById(id).get();
		 
		 if(elasticMerchant==null)
			  throw new NoSuchElementException("mrchant is exist of this id="+ id);
		 
		 elasticMerchant.setAddress(merchant.getAddress());
		 elasticMerchant.setEmail(merchant.getEmail());
		 elasticMerchant.setMobileNo(merchant.getMobileNo());
		 elasticMerchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 elasticMerchant.setShopName(merchant.getShopName());
		 
		 elasticMerchantRepository.save(elasticMerchant);
		 
		 //HERE WE WILL UPDATE ONLY THOSE FIELDS WHICH ARE UPDATABLE 
		 Merchant tempmarchant = merchantRepository.findById(id).get();
		 tempmarchant.setAddress(merchant.getAddress());
		 tempmarchant.setEmail(merchant.getEmail());
		 tempmarchant.setMobileNo(merchant.getMobileNo());
		 tempmarchant.setPassword(bcrptEncoder.encode(merchant.getPassword()));
		 tempmarchant.setShopName(merchant.getShopName());
		 
		 merchantRepository.save(tempmarchant); 
	 }
	 public void deleteMerchantAccount(Long id)
	 {
		 merchantRepository.deleteById(id);
		 elasticMerchantRepository.deleteById(id);		 
	 }
   
}
