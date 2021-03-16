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


//To HANDLE ALL REQUEST RELATED TO PERSON, PERSONCONTROLLER CLASS CREATED
// MARKED AS RESTCONTROLLER
@RestController                                                                   
public class PersonController 
{       
	// LOGGER IS USED TO PRINT THE VALUE OF VARIABLE LIKE SYSTEM.OUT.PRINTLN METHOD, BUT IT ALSO PRINT DATE, TIME , CLASS WITH PACKAGE THAT HELPS IN TRACING. 
	Logger logger=LoggerFactory.getLogger(PersonController.class);
	
	//TO INJECT(PUSH HERE) OBJECT OF A CLASS(FROM APPLICATION CONTEXT{IOC CONTAINER), AUTOWIRED IS USED.
	@Autowired
	private PersonRequestBodyValidator personRequestBodyValidator;
	@Autowired                                                                     
	private PersonService personService;                                             

         // TO MAP URL WITH METHOD(RESPONSIBLE FOR SERVE THE USER REQUEST), GETMAPPING ANNO. IS USED. 
	 // THIS IS A API WHICH WILL RETREIVE ALL PERSON RECORDS FROM DATABASE.
	 @GetMapping("/persons")
	 public ResponseEntity<Iterable<ElasticPerson>> fatchAllPerson() {
	     try {
	    	  
	    	 Iterable<ElasticPerson> persons = personService.listAllPerson();
	    	 logger.info("responsed person {}",persons); 
	         return new ResponseEntity<Iterable<ElasticPerson>> (persons, HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	    	 logger.error("Exception occured, "+e.getMessage());
	         return new ResponseEntity<Iterable<ElasticPerson>>(HttpStatus.NOT_FOUND);
	     } catch(Exception e)
	     {
	    	 logger.error("Exception occured, "+e.getMessage());
	    	 return new ResponseEntity<Iterable<ElasticPerson>>(HttpStatus.NOT_FOUND);
	     }
	 }
	
	  // THIS IS A API WHICH WILL RETREIVE A PERSON RECORDS FROM DATABASE.
	 @GetMapping("/person/{id}")
	 public ResponseEntity<ElasticPerson> get(@PathVariable Long id) {
	     try {
	    	 //TO VALIDATE PASSED ID 
	    	  personRequestBodyValidator.personRequestIdValidation(id);
	    	  
	    	 ElasticPerson person = personService.getPerson(id);
	    	 logger.info("responsed person {}",person);
	    	 
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
	
	 // THIS METHOD WILL REGISTER THE PERSON.
	 @PostMapping("/Sign-Up")
	 public ResponseEntity<String> registerPerson(@RequestBody PersonRequestEntity user) {
		 logger.info("PersonRequestEntity received from user {}",user);
	     try
	     {
	    	 //TO VALIDATE REQUEST BODY'S FIELDS VALID OR NOT
	    	 personRequestBodyValidator.createPersonValidation(user);
	    	 
	    	 personService.saveUser(user);	 
	         return new ResponseEntity<String>("person registered successfully And userName= "+user.getUserName()+", password= "+user.getPassword(),HttpStatus.OK);
	     }
	     catch(Exception e)
	     {
	        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	     }
	 }
	 
	 
	// TO UPDATE CERTAIN PERSON RECORD USING ID.
	 @PutMapping("/person/{id}")
	 public ResponseEntity<String> toUpdatePersonDetail(@RequestBody UpdatePersonRequest person,@PathVariable Long id) {
	     try
	     {   
	    	 //TO CHECK WETHER PASSED PARAMETERS ARE VALID OR NOT
	    	 personRequestBodyValidator.personRequestBodyAndIdValidation(person,id);
	    	 
	    	 logger.info("person received as a request body {}",person);
	    	 
	    	 personService.updatePerson(person,id);
	         return new ResponseEntity<String>(" Record of the given id's person has been updated ",HttpStatus.OK);
	     } catch (Exception e) {
	         return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	     }      
	 } 
	
	// TO DELETE CERTAIN PERSON RECORD USING ID.
	 @DeleteMapping("/person/{id}")
	 public ResponseEntity<Person> deletePersonUsingId(@PathVariable Long id)
	 {
		 try
		 {
			// TO VALIDATE PASSED ID 
			personRequestBodyValidator.personRequestIdValidation(id);
			
		    Person person= personService.getPersonMysql(id);  
		    personService.deletePerson(id);
		    logger.info("deleted person {}, " + person);
		    return new ResponseEntity<Person>(person,HttpStatus.OK);      
	     }
		     catch(Exception e) {
			 logger.error("Exception occured, "+e.getMessage());
			 return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		  }
	 }
}
