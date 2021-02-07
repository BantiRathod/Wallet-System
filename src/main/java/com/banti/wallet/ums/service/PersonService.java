package com.banti.wallet.ums.service;

import java.util.Date;
import java.util.List;
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


@Service
@Transactional
public class PersonService {
	
	@Autowired
	private ElasticPersonRepository elasPersonRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
    @Autowired
    private PersonRepository personRepo;
    
    
    public Person findByMobileNo(String mobileNo) {
       return personRepo.findByMobileNo(mobileNo);
    }
    
    public List<Person> listAll() {
        return personRepo.findAll();
    }
     
    public void updateUser(Person user) throws NoSuchElementException
    {
    	
    	 Person existUser =  personRepo.findById(user.getUserId()).get();
         existUser.setUserName(user.getUserName());                         
         existUser.setFirstName(user.getFirstName());                       
         existUser.setLastName(user.getLastName());                          
         existUser.setAddress(user.getAddress());
         existUser.setMobileNo(user.getMobileNo());
         personRepo.save(existUser);
         
    }
    
    public void saveUser(PersonRequestEntity user) throws Exception {
    	
    	//FATCH FROM ELASTICSEARCH DATABASE
    	ElasticPerson existPerson = elasPersonRepository.findByMobileNo(user.getMobileNo());
        //Person existUser = personRepo.findByMobileNo(user.getMobileNo());
        if(existPerson!=null)
        	throw new Exception("person is already exist"); 
        
        
      
    	Person realUser = new Person();	
    	realUser.setPassword(bcryptEncoder.encode(user.getPassword()));          // save password in encrypted forms
    	realUser.setUserName(user.getUserName());
    	realUser.setAddress(user.getAddress());
    	realUser.setFirstName(user.getFirstName());
    	realUser.setLastName(user.getLastName());
    	realUser.setMobileNo(user.getMobileNo());
    	realUser.setEmail(user.getEmail());
    	realUser.setRegisterDate(new Date());
    	realUser.setStatus(PersonStatus.ACTIVE.name());
    	// TO SAVE RECORD IN MYSQL DATABASE
        personRepo.save(realUser); 
        
        ElasticPerson elasticPerson = new ElasticPerson();
        elasticPerson.setUserName(user.getUserName());
        elasticPerson.setAddress(user.getAddress());
        elasticPerson.setEmail(user.getEmail());
        elasticPerson.setFirstName(user.getFirstName());
        elasticPerson.setLastName(user.getLastName());
        elasticPerson.setMobileNo(user.getMobileNo());
        elasticPerson.setPassword(bcryptEncoder.encode(user.getPassword()));
        elasticPerson.setRegisterDate(new Date());
        elasticPerson.setStatus(PersonStatus.ACTIVE.name());
        // TO SAVE RECORD IN ELASTICSEARCH DATABASE
        elasPersonRepository.save(elasticPerson);
                                                                                              
    }
     
    public Person get(Long id) {
        return personRepo.findById(id).get();
    }
     
    public void delete(Long id) {
    	personRepo.deleteById(id);
    }
    
    public Person findUserByUserName(String username)
    {
    	return personRepo.findUserByUserName(username);
    }
}