package com.banti.wallet.ums.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.repository.MerchantRepository;

@Service
@Transactional
public class MerchantService
{
  @Autowired
  private MerchantRepository merchantRepository;
  
   public List<Merchant> getAll()
	 {
		 return merchantRepository.findAll();
	 }
	 
	 public Merchant get(Long id)
	 {
	  return  merchantRepository.findById(id).get();
	 }
	
	 public void createOrUpdate(Merchant merchant)
	 {
		 merchantRepository.save(merchant);
	 }	
	 
   
}
