package com.banti.wallet.ums.validator.business;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.controller.MerchantController;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.repository.MerchantWalletRepository;
import com.banti.wallet.ums.requestEntities.MerchantWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantWalletRequest;


@Service
public class MerchantWalletBusinessValidator {

	@Autowired
	private MerchantWalletRepository merchantWalletRepository;
	
	Logger logger=LoggerFactory.getLogger(MerchantWalletBusinessValidator.class);
	
	public void createMerchantWalletValidation(MerchantWalletRequest merchantWallet) throws Exception 
	{
		try
		{
		 MerchantWallet tempMerchantWallet = merchantWalletRepository.findById(merchantWallet.getMobileNo()).get();
		 if(tempMerchantWallet!=null)
			 throw new Exception("merchant account is already oppened with this mobile number "+ merchantWallet.getMobileNo());
		}catch(NoSuchElementException e)
		{
			logger.error("merchant wallet can be created");
		} 
	}
	
	
	public void updateMerchantWalletValidation(UpdateMerchantWalletRequest merchantWallet, String mobileNo) throws Exception {
		MerchantWallet existmerchantWallet = merchantWalletRepository.findById(mobileNo).get();
		
		 if(existmerchantWallet==null)
		    	 throw new Exception("account not opened yet by this mobile number");
		
	}
}
