package com.banti.wallet.ums.validator.business;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.repository.MerchantRepository;
import com.banti.wallet.ums.requestEntities.MerchantRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantRequest;

@Service
public class MerchantBusinessValidator {
	
	@Autowired
	private MerchantRepository  merchantRepository;
      
	public void updateMerchantvalidation(UpdateMerchantRequest merchant, Long id) throws Exception
	{
          Merchant existMerchant = merchantRepository.findById(id).get();
		 if(existMerchant==null)
			  throw new NoSuchElementException("Merchant Account is exist of this id ="+ id);
	}

	public void createMerchantValidation(MerchantRequest merchant) throws Exception{
		
		 if(merchantRepository.findByMobileNo(merchant.getMobileNo())!=null)
			  throw new Exception("Merchant account is already exist with this mobile number"+merchant.getMobileNo());
	}
}
