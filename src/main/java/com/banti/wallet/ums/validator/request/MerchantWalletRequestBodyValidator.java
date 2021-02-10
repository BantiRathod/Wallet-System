package com.banti.wallet.ums.validator.request;

import org.springframework.stereotype.Service;

import com.banti.wallet.ums.requestEntities.UpdateMerchantWalletRequest;

@Service
public class MerchantWalletRequestBodyValidator {
	
	public void  merchantWalletIdValidatoion(String mobileNo) throws Exception
	{
		if(mobileNo.length()!=10)
			   throw new Exception("Invalid mobile number passed, " +mobileNo);
	}

	public void updateMerchantWalletValidation(String mobileNo,UpdateMerchantWalletRequest updateMerchantWalletRequest) throws Exception {
		
		if(updateMerchantWalletRequest.getMobileNo().length()!=10 || mobileNo.length()!=10 )
			throw new Exception("Invalid mobile number passed, " + updateMerchantWalletRequest.getMobileNo());
			
			
	}

}
