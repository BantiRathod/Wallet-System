package com.banti.wallet.ums.validator.request;

import org.springframework.stereotype.Service;

import com.banti.wallet.ums.requestEntities.PersonRequestEntity;
import com.banti.wallet.ums.requestEntities.UpdatePersonRequest;

@Service
public class PersonRequestBodyValidator {

	public void personRequestIdValidation(Long id) throws Exception
	{
		if(id<0)
			throw new Exception("Invalid Id passed , "+ id);
	}

    //WHEN CLIENT MAKES REQUEST FOR PERSON DETAILS UPDATION 
	public void personRequestBodyAndIdValidation(UpdatePersonRequest person, Long id) throws Exception{
		if(id<0)
			throw new Exception("Invalid Id passed , "+ id);
		
		if(person.getMobileNo().length()!=10)
			throw new Exception("Invalid MobileNo found , "+ person.getMobileNo());
		
	}

	public void createPersonValidation(PersonRequestEntity person) throws Exception{
		
		if(person.getMobileNo().length()!=10)
			throw new Exception("Invalid MobileNo found , "+ person.getMobileNo());
		
	}
}
