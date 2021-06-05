package com.banti.wallet.ums.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;
import com.banti.wallet.ums.elasticsearch.repositories.ElasticMerchantRepository;
import com.banti.wallet.ums.enums.PersonStatus;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.repository.MerchantRepository;
import com.banti.wallet.ums.requestEntities.MerchantRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantRequest;
import com.banti.wallet.ums.validator.business.MerchantBusinessValidator;

@Service
@Transactional
public class MerchantService
{
  
  @Autowired
  private MerchantBusinessValidator merchantBusinessValidator;

	/*
	 * private PasswordEncoder bcrptEncoder; 
	 * @Autowired
	 */
  @Autowired
  private MerchantRepository merchantRepository;
  @Autowired
  private ElasticMerchantRepository elasticMerchantRepository;
 
   public Iterable<ElasticMerchant> getListOfAllMerchants()
	 {
		 return  elasticMerchantRepository.findAll();
	 }
	 

	public Merchant findByMobileNoFromMysql(String payeeMobileNo) {
		return  merchantRepository.findByMobileNo(payeeMobileNo) ;
	}
	
	
	 public ElasticMerchant getMerchant(Long id) throws Exception
	 {
	      return elasticMerchantRepository.findById(id).get();
	 }
	

	public ElasticMerchant findByMobileNo(String payeeMobileNo) {
			return elasticMerchantRepository.findByMobileNo(payeeMobileNo);
	 }	
	
	 public void deleteMerchantAccount(Long id)
	 {
		 merchantRepository.deleteById(id);
		 elasticMerchantRepository.deleteById(id);		 
	 }

	public Merchant getMerchantFromMysql(Long id) {
		return merchantRepository.findById(id).get();
	}
		
	
	 public void createMerchantAccount(MerchantRequest merchant) throws Exception
	 {
		 // TO BUSINESS VALIDATION
		 merchantBusinessValidator.createMerchantValidation(merchant);
		
		 
		 Merchant tempMerchant = new Merchant();
		 tempMerchant.setMobileNo(merchant.getMobileNo());
		 tempMerchant.setShopName(merchant.getShopName());
		 tempMerchant.setAddress(merchant.getAddress());
		 tempMerchant.setEmail(merchant.getEmail());
		 tempMerchant.setRegisterDate(new Date());
		 tempMerchant.setStatus(PersonStatus.ACTIVE.name());
		 // MERCHANT RECORD HAS BEEN STORED IN MYSQL
		Merchant returnMerchant = merchantRepository.save(tempMerchant);
		
		 
		ElasticMerchant elasticMerchant = new ElasticMerchant();
		 elasticMerchant.setAddress(merchant.getAddress());
		 elasticMerchant.setRegisterDate(tempMerchant.getRegisterDate());
		 elasticMerchant.setEmail(merchant.getEmail());
		 elasticMerchant.setMobileNo(merchant.getMobileNo());
		 elasticMerchant.setShopName(merchant.getShopName());
		 elasticMerchant.setStatus(PersonStatus.ACTIVE.name());
		 elasticMerchant.setMerchantId( returnMerchant.getMerchantId());
		 // MERCHANT RECORD HAS BEEN STORED IN ELASTICSEARCH DATABASE
		 elasticMerchantRepository.save(elasticMerchant);
	 
	 }

	 
	 
	 public void updateMerchantAccount(UpdateMerchantRequest merchant, Long id) throws Exception
	 {
		 //BUSINESS VALIDATION
		 merchantBusinessValidator.updateMerchantvalidation(merchant, id);
		
		 Merchant tempmarchant = merchantRepository.findById(id).get();
		 
		 //DONT SAVE PROXY OBJECT RETREIVED FROM DATA BASE INSTEAD OF IT PUSH NEW OBJECT
		 Merchant newMerchant=new Merchant();
		 newMerchant.setRegisterDate(tempmarchant.getRegisterDate());
		 newMerchant.setStatus(tempmarchant.getStatus());
		 newMerchant.setAddress(merchant.getAddress());
		 newMerchant.setEmail(tempmarchant.getEmail());
		 newMerchant.setMobileNo(tempmarchant.getMobileNo());
		 newMerchant.setMerchantId(id);
		 newMerchant.setShopName(merchant.getShopName());
		 
		 merchantRepository.save(newMerchant); 
		 
		//HERE WE WILL UPDATE ONLY THOSE FIELDS WHICH ARE UPDATABLE 
		 ElasticMerchant elasticMerchant = new ElasticMerchant();
		 elasticMerchant.setAddress(merchant.getAddress());
		 elasticMerchant.setEmail(tempmarchant.getEmail());
		 elasticMerchant.setMobileNo(tempmarchant.getMobileNo());
         elasticMerchant.setMerchantId(id);
         elasticMerchant.setRegisterDate( tempmarchant.getRegisterDate());
         elasticMerchant.setStatus(tempmarchant.getStatus());
		 elasticMerchant.setShopName(merchant.getShopName());
		 
		 elasticMerchantRepository.save(elasticMerchant);
	 }

   
}
