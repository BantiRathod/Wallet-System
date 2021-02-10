package com.banti.wallet.ums.validator.request;

import org.springframework.stereotype.Service;

import com.banti.wallet.ums.requestEntities.PersonWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdatePersonWalletRequest;

@Service
public class PersonWalletRequestBodyValidator 
{

	public void  createPersonWalletValidation(PersonWalletRequest personWalletRequest ) throws Exception
	{
		if( personWalletRequest.getBalance() < 0)
			 throw new Exception("invalid amount, "+personWalletRequest.getBalance()) ;
		
		if(personWalletRequest.getMobileNo().length()!=10)
			 throw new Exception("invalid mobile number , "+personWalletRequest.getMobileNo()) ;
	}

	public void personWalletMobileNoValidation(UpdatePersonWalletRequest personWallet, String mobileNo) throws Exception {
		   if((personWallet!=null) && personWallet.getMobileNo().length()!=10)
	        	 throw new Exception("\"invalid mobile number ,"+ personWallet.getMobileNo());
		   else if(mobileNo.length()!=10)
			   throw new Exception("\"invalid mobile number ,"+ personWallet.getMobileNo());
			      
	}
}
