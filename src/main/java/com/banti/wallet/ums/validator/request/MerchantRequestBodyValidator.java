package com.banti.wallet.ums.validator.request;

import org.springframework.stereotype.Service;

import com.banti.wallet.ums.requestEntities.MerchantRequest;

@Service
public class MerchantRequestBodyValidator {

	public void merchantRequestIdValidation(Long  id) throws Exception
	{
		if(id<0)
			throw new Exception("Invalid Id passed ");
	}

	public void createMerchantValidation(MerchantRequest merchant) throws Exception {
		
		if(merchant.getMobileNo().length()!=10 || merchant.getMobileNo().isEmpty())
			 throw new Exception("Invalid Mobile Number passed, "+merchant.getMobileNo());
		
	}
	
}
