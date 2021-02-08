package com.banti.wallet.ums.validator.request;

import com.banti.wallet.ums.requestEntities.PersonWalletRequest;

public class PersonWalletRequestValidator {

	public void personWalletRequestValidation(PersonWalletRequest personWallet) throws Exception
	{
		if(personWallet.getMobileNo().length()!= 10)
			throw new Exception("Invalid mobile Number entered, "+personWallet.getMobileNo());
		
		if(personWallet.getBalance()<0)
			throw new Exception("Invalid wallet balance , "+ personWallet.getBalance());
	}
}
