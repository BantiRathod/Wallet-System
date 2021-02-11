package com.banti.wallet.ums.validator.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.repository.PersonWalletRepository;
import com.banti.wallet.ums.requestEntities.UpdatePersonWalletRequest;

@Service
public class PersonWalletBusinessValidator {

	@Autowired
	private PersonWalletRepository  personWalletRepository;
	
	
	public void updatePersonWalletValidatoion(UpdatePersonWalletRequest personWallet, String mobileNo) throws Exception
	{
		
		 PersonWallet existpersonWallet  = personWalletRepository.findById(mobileNo).get();
		 if(existpersonWallet==null)
			 throw new Exception("PERSON WALLET is OPENED YET OF THIS NUMBER,"+mobileNo);
	}
}
