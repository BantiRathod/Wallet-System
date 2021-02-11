package com.banti.wallet.ums.validator.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.repository.PersonRepository;
import com.banti.wallet.ums.requestEntities.PersonRequestEntity;
import com.banti.wallet.ums.requestEntities.UpdatePersonRequest;

@Service
public class personBusinessValidator {

	@Autowired
	private PersonRepository personRepository;
	
	public void updatePersonValidation(UpdatePersonRequest person, Long id) throws Exception
	{
		 Person existPerson = personRepository.findById(id).get();
    	 if(existPerson ==null)
    		  throw new Exception("User is not exist with of Id number ");
	}

	public void createPersonValidation(PersonRequestEntity person) throws Exception {
		 Person existPerson = personRepository.findByMobileNo(person.getMobileNo());
		 if(existPerson!=null)
   		   throw new Exception("User is already registerd ");
		 
		
	}
}
