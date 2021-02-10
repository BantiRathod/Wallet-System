package com.banti.wallet.ums.validator.request;

import com.banti.wallet.ums.requestEntities.PersonWalletRequest;

public class PersonWalletRequestValidator 
{

	public void  personWalletRequestValidation(PersonWalletRequest personWalletRequest ) throws Exception
	{
		if( personWalletRequest.getBalance() < 0)
			 throw new Exception("invalid amount, "+personWalletRequest.getBalance()) ;
		
		if(personWalletRequest.getMobileNo().length()!=10)
			 throw new Exception("invalid mobile number , "+personWalletRequest.getMobileNo()) ;
	}
}
