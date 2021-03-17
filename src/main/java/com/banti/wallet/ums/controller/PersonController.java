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


//Controller class, TO HANDLE ALL REQUESTS RELATED TO PERSON RECORDS, MARKED WITH RESTCONTROLLER.
//To HANDLE ALL REQUEST RELATED TO PERSON, PERSONCONTROLLER CLASS CREATED
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
	
	 /**
	  * THIS API IS REPONSIBLE FOR RETREIVING ALL REGISTERED PERSON RECORDS.
	  * 
	  * HERE, METHOD OF PERSON SERVICE LAYER WOULD BE INVOKED IN ORDER TO GET PERSON RECORDS....
	  * AND PERSON RECORDS WOULD BE RETRIEVED FROM ELASTICSEARCH DATA BASE INSTEAD OF MYSQL.  
	  * 
	  * IF RECORDS NOT EXIST THEN RETURN HTTPSTATUS "NOT_FOUND".   
	  */
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
	
	 
	 /**
	  * THIS API IS REPONSIBLE FOR RETRIEVING A PERSON RECORD.
	  * 
	  * @param id IS A PARAMETER, WOULD BE PASSED WITH MAKING REQUEST BY USER.
	  * BY REQUESTBODYVALIDATOR CLASS, PASSED PARAMETER WOULD BE VARIFY, IF OK THEN PROCESS FURTHER...( CONTINUE STATEMENT WITH NEW LINE)
	  * WOULD CALL SERVICE LAYER METHOD.
	  * 
	  * @return PERSON RECORD WOULD BE RETURN IF EXIST, ONTHERWISE AN EXCEPTION WOULD BE THROWN AND RETURN HTTPSTATUS CODE "NOT_FOUND". 
	  */ 
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
	
	 
	 /**
	  * THIS API IS REPONSIBLE FOR, TO DONE USER REGISTRATION.
	  * 
	  * PostMapping ANNOTATION MAKE THE SPECIFIED URL WITH METHOD.
	  * 
	  * @param user PARAMETER TYPE OF PersonRequestEntity WPOULD BE PASSED WHILE MAKING URL REQUEST BY USER.
	  * 
	  * USER PARAMETER WOULD BE VALIDAED BY personRequestBodyValidator CLASS,IF PASSED VALUES ARE RIGHT...
	  * THEN INVOKE METHOD OF SERVICE LAYER FOR SAVING USER DETAILS, OTHERWISE THROW AN EXCEPTION.
	  * 
	  * @return A MESSAGE WOULD BE RETURN AFTER SUCCESSFUL REGISTRATION, OTHERWISE A EXCEPTION MESSAGE RETURN. 
	  */
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
	 
	 
	/**
	 * THIS API IS REPONSIBLE FOR UPDATING EXIST PERSON RECORD.
	 * 
	 * 
	 * @param person TYPE OF UpdatePersonRequest AND id WPOULD BE PASSED WHILE MAKING URL REQUEST BY USER.
	 * 
	 * personRequestBodyValidator CLASS WOULD VALIDET THE PASSED PARAMETER,IF PASSED VALUES ARE RIGHT...
	 * THEN INVOKE METHOD OF SERVICE LAYER FOR UPDATING USER DETAILS, OTHERWISE THROW AN EXCEPTION.
	 * 
	 * @return STRING WOULD BE RETURN AFTER SUCCESSFUL DELETION OR EXCEPTION MESSAGE. 
	 */
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
	
	 

     /**
      * THIS API IS REPONSIBLE FOR, DELETING EXIST USER RECORD.
	  * 
	  * DeleteMapping ANNOTATION MAP THE SPECIFIED URL WITH METHOD.
	  * 
	  * @param ID WPOULD BE PASSED WHILE MAKING URL REQUEST BY USER.
	  * 
	  * id PARAMETER WOULD BE VALIDAED BY personRequestBodyValidator CLASS,IF PASSED VALUE is RIGHT...
	  * THEN INVOKE METHOD OF SERVICE LAYER FOR deleting PERSON DETAILS, OTHERWISE THROW AN EXCEPTION PERSON NOT EXIST.
	  * 
	  * @return A DELETED PERSON WOULD BE RETURN AFTER SUCCESSFUL DELETION, OTHERWISE RETURN A HTTTPSTATUS NOT FOUND.
      */
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
