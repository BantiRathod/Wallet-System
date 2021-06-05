package com.banti.wallet.ums.service;

import java.util.Date;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.elasticsearch.repositories.ElasticPersonRepository;
import com.banti.wallet.ums.enums.PersonStatus;
import com.banti.wallet.ums.model.Person;

import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;
import com.banti.wallet.ums.repository.PersonRepository;
import com.banti.wallet.ums.requestEntities.PersonRequestEntity;
import com.banti.wallet.ums.requestEntities.UpdatePersonRequest;
import com.banti.wallet.ums.validator.business.personBusinessValidator;


@Service
@Transactional
public class PersonService  {
	
	Logger logger=LoggerFactory.getLogger(PersonService.class);
	@Autowired
	private ElasticPersonRepository elasPersonRepository;
	@Autowired
	private personBusinessValidator personBusinessValidator;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
    @Autowired
    private PersonRepository personRepository;
    
    
    public ElasticPerson findByMobileNo(String mobileNo) {
       return elasPersonRepository.findByMobileNo(mobileNo);
    }
    
    public Iterable<ElasticPerson> listAllPerson() {
        return elasPersonRepository.findAll();
    }
    
    public ElasticPerson getPerson(Long id) {
        return elasPersonRepository.findById(id).get();
    }
     
    public void deletePerson(Long id) throws NoSuchElementException {
    	elasPersonRepository.deleteById(id);
    	personRepository.deleteById(id);
    }
    
    public ElasticPerson findUserByUserName(String username)
    {
    	return elasPersonRepository.findUserByUserName(username);
    }

    public Person findPersonByMobileNoMysql(String moileNo)
    {
    	return personRepository.findByMobileNo(moileNo);
    }
    
	public Person getPersonMysql(Long id) {
		return personRepository.findById(id).get();
	}
    //TO UPDATE PERSON RECORD
    public void updatePerson(UpdatePersonRequest person, Long id) throws Exception
    {
    	//TO BUSINESS VALIADTE FOR UPDTE PDERSON
    	personBusinessValidator.updatePersonValidation(person,id);
    	
    	 Person existPerson =  personRepository.findById(id).get();
    	 logger.info("exist person {}",existPerson);
    	 
    	// Dont MAKE CHANGES IN RETRIEVED OBJECT, INSTEAD OF IT CREATE NEW OBJECT, INSIALIZE ALL FIELDS AND SAVE IT.
    	 Person newPerson = new Person();
    	 
    	 newPerson.setUserId(id);
    	 
    	 if(person.getAddress()!="")
    	     newPerson.setAddress(person.getAddress());
    	 else
    		 newPerson.setUserName(existPerson.getAddress());
    	 if(person.getUserName()!="")
    	     newPerson.setUserName(person.getUserName());
    	 else
    		 newPerson.setUserName(existPerson.getUserName());
    	 if(person.getFirstName()!="")
    	     newPerson.setFirstName(person.getFirstName()); 
    	 else
    		 newPerson.setFirstName(existPerson.getFirstName());
    	 if(person.getLastName()!="")
    	     newPerson.setLastName(person.getLastName()); 
    	 else
             newPerson.setAddress(existPerson.getLastName());
         if(person.getPassword()!="")
        	 newPerson.setPassword(bcryptEncoder.encode(person.getPassword()));
         else
        	 newPerson.setPassword(existPerson.getPassword());
         
         newPerson.setEmail(existPerson.getEmail());
         newPerson.setMobileNo(existPerson.getMobileNo());
         newPerson.setRegisterDate(existPerson.getRegisterDate());
         newPerson.setStatus(existPerson.getStatus());
        
         personRepository.save(newPerson);
    	 
         //ElasticPerson elasaticPerson =  elasPersonRepository.findById(id).get();
         ElasticPerson elasaticPerson = new ElasticPerson();
         elasaticPerson.setUserId(id);
         if(person.getAddress()!="")
        	 elasaticPerson.setAddress(person.getAddress());
    	 else
    		 elasaticPerson.setAddress(existPerson.getAddress());
         if(person.getUserName()!="")
        	 elasaticPerson.setUserName(person.getUserName());
    	 else
    		 elasaticPerson.setUserName(existPerson.getUserName());
    	 if(person.getFirstName()!="")
    		 elasaticPerson.setFirstName(person.getFirstName()); 
    	 else
    		 elasaticPerson.setFirstName(existPerson.getFirstName());
    	 if(person.getLastName()!="")
    		 elasaticPerson.setLastName(person.getLastName()); 
    	 else
    		 elasaticPerson.setAddress(existPerson.getLastName());
         if(person.getPassword()!="")
        	 elasaticPerson.setPassword(bcryptEncoder.encode(person.getPassword()));
         else
        	 elasaticPerson.setPassword(existPerson.getPassword());
         
    	 
    	 elasaticPerson.setRegisterDate(existPerson.getRegisterDate());
    	 elasaticPerson.setStatus(existPerson.getStatus());
    	 elasaticPerson.setMobileNo(existPerson.getMobileNo());

    	 elasPersonRepository.save(elasaticPerson);
           
    }
    
    //TO STORE A PERSON RECORD
    public void saveUser(PersonRequestEntity person) throws Exception
    {
    	//TO CHECk THAT, USER IS ALREADY EXIST OR NOT
    	personBusinessValidator.createPersonValidation(person);
    	
        Person realUser=new Person();
    	realUser.setPassword(bcryptEncoder.encode(person.getPassword()));         
    	realUser.setUserName(person.getUserName());
    	realUser.setAddress(person.getAddress());
    	realUser.setFirstName(person.getFirstName());
    	realUser.setLastName(person.getLastName());
    	realUser.setMobileNo(person.getMobileNo());
    	realUser.setEmail(person.getEmail());
    	realUser.setRegisterDate(new Date());
    	realUser.setStatus(PersonStatus.ACTIVE.name());
    	//TO SAVE RECORD IN MYSQL DATABASE
    	Person tempPerson=personRepository.save(realUser);
        
    	
        ElasticPerson elasticPerson = new ElasticPerson();
        elasticPerson.setUserName(person.getUserName());
        elasticPerson.setAddress(person.getAddress());
        elasticPerson.setEmail(person.getEmail());
        elasticPerson.setUserId(tempPerson.getUserId());
        elasticPerson.setFirstName(person.getFirstName());
        elasticPerson.setLastName(person.getLastName());
        elasticPerson.setMobileNo(person.getMobileNo());
        elasticPerson.setPassword(bcryptEncoder.encode(person.getPassword()));
        elasticPerson.setRegisterDate(realUser.getRegisterDate());
        elasticPerson.setStatus(PersonStatus.ACTIVE.name());  
        // TO SAVE RECORD IN ELASTICSEARCH DATABASE
        elasPersonRepository.save(elasticPerson);
                                                                                              
    }

}
    