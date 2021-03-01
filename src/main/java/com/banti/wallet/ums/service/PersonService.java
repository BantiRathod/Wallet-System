package com.banti.wallet.ums.service;

import java.util.Date;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;
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
    	// Dont MAKE CHANGES IN RETRIEVED OBJECT, INSTEAD OF IT CREATE NEW OBJECT THEN SAVE IT
    	 Person newPerson = new Person();
    	 
    	 newPerson.setUserId(id);
    	 newPerson.setUserName(person.getUserName());                         
    	 newPerson.setFirstName(person.getFirstName());                       
    	 newPerson.setLastName(person.getLastName());                          
         newPerson.setAddress(person.getAddress());
         newPerson.setMobileNo(person.getMobileNo());
         newPerson.setRegisterDate(existPerson.getRegisterDate());
         newPerson.setStatus(existPerson.getStatus());
         newPerson.setPassword(bcryptEncoder.encode(person.getPassword()));
         personRepository.save(newPerson);
    	 
         //ElasticPerson elasaticPerson =  elasPersonRepository.findById(id).get();
         ElasticPerson elasaticPerson = new ElasticPerson();
         elasaticPerson.setUserId(id);
    	 elasaticPerson.setUserName(person.getUserName());                         
    	 elasaticPerson.setFirstName(person.getFirstName());                       
    	 elasaticPerson.setLastName(person.getLastName());                          
    	 elasaticPerson.setAddress(person.getAddress());
    	 elasaticPerson.setRegisterDate(elasaticPerson.getRegisterDate());
    	 elasaticPerson.setStatus(elasaticPerson.getStatus());
    	 elasaticPerson.setMobileNo(person.getMobileNo());
    	 elasaticPerson.setPassword(bcryptEncoder.encode(person.getPassword()));
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
    