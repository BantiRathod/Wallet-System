package com.banti.wallet.ums.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.requestEntities.PersonRequestEntity;
import com.banti.wallet.ums.requestEntities.UpdatePersonRequest;
import com.banti.wallet.ums.service.PersonService;
import com.banti.wallet.ums.validator.request.PersonRequestBodyValidator;

import java.util.NoSuchElementException;


//TO WORK AT SERVER SIDE AND REMOVE VIEW PART Restcontroller USED 
@RestController                                                                   
public class PersonController 
{
	Logger logger=LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	private PersonRequestBodyValidator personRequestBodyValidator;
	//TO INJECT(PUSH HERE) PERSONSERVICE CLASS OBJECT( FROM APPLICATION CONTEXT{IOC CONTAINER})
	@Autowired                                                                     
	private PersonService personService;                                             

   
	 @GetMapping("/persons")
	 public Iterable<ElasticPerson> fatchAllPerson()
	 {
	    return personService.listAllPerson();
	 }
	 
	
	 @GetMapping("/person/{id}")
	 public ResponseEntity<ElasticPerson> get(@PathVariable Long id) {
	     try {
	    	  personRequestBodyValidator.personRequestIdValidation(id);
	    	 ElasticPerson person = personService.getPerson(id);
	         return new ResponseEntity<ElasticPerson> (person, HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	    	 logger.error("Exception occured, "+e.getMessage());
	         return new ResponseEntity<ElasticPerson>(HttpStatus.NOT_FOUND);
	     } catch(Exception e)
	     {
	    	 logger.error("Exception occured, "+e.getMessage());
	    	 return new ResponseEntity<ElasticPerson>(HttpStatus.NOT_FOUND);
	     }
	 }
	
	 
	 @PostMapping("/Sign-Up")
	 public ResponseEntity<String> registerPerson(@RequestBody PersonRequestEntity user) {
		 logger.info("PersonRequestEntity received from user {}",user);
	     try
	     {
	    	 
	    	 personService.saveUser(user);	 
	         return new ResponseEntity<String>("person registered successfully And userName= "+user.getUserName()+", password= "+user.getPassword(),HttpStatus.OK);
	     }
	     catch(Exception e)
	     {
	        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	     }
	 }
	 
	 
	// RESTful API for Update Operation
	 @PutMapping("/person/{id}")
	 public ResponseEntity<String> toUpdatePersonDetail(@RequestBody UpdatePersonRequest person,@PathVariable Long id) {
	     try
	     {   
	    	 //TO CHECK PASSED PARAMETERS ARE VALID OR NOT
	    	 personRequestBodyValidator.personRequestBodyAndIdValidation(person,id);
	    	 
	    	 logger.info("person received as a request body {}",person);
	    	 
	    	 personService.updatePerson(person,id);
	         return new ResponseEntity<String>(" Record of the given id's person has been updated ",HttpStatus.OK);
	     } catch (Exception e) {
	         return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	     }      
	 } 
	
	// RESTful API for Delete Operation
	 @DeleteMapping("/person/{id}")
	 public ResponseEntity<Person> deletePersonUsingId(@PathVariable Long id)
	 {
		 try
		 {
			personRequestBodyValidator.personRequestIdValidation(id);
		    Person person= personService.getPersonMysql(id);  
		    personService.deletePerson(id);
		    logger.info("deleted person {}, " + person);
		   return new ResponseEntity<Person>(person,HttpStatus.OK);      
	     }
		  catch(Exception e)
		  {
			 logger.error("Exception occured, "+e.getMessage());
			 return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		  }
	 }
}
