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
import java.util.NoSuchElementException;



@RestController                                                                    //work at server side and remove view part
public class PersonController 
{
	Logger logger=LoggerFactory.getLogger(PersonController.class);
	
	@Autowired                                                                     
	private PersonService personService;                                             

     //RESTful API for getting all users
	 @GetMapping("/Persons")
	 public Iterable<ElasticPerson> fatchAllPerson()
	 {
	    return personService.listAllPerson();
	 }
	 
	//RESTful API for getting the record particular user
	 @GetMapping("/person/{id}")
	 public ResponseEntity<ElasticPerson> get(@PathVariable Long id) {
	     try {
	    	 ElasticPerson person = personService.getPerson(id);
	         return new ResponseEntity<ElasticPerson> (person, HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<ElasticPerson>(HttpStatus.NOT_FOUND);
	     }      
	 }
	
	 //RESTful API for Create data
	 @PostMapping("/Sign-Up")
	 public ResponseEntity<String> registerPerson(@RequestBody PersonRequestEntity user) {
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
	 public ResponseEntity<String> update(@RequestBody UpdatePersonRequest person,@PathVariable Long id) {
	     try
	     {
	    	 logger.info("person received as a request body {}",person);
	    	 personService.updatePerson(person,id);
	         return new ResponseEntity<String>(" Record of the given id's person has been updated ",HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	     }      
	 } 
	
	// RESTful API for Delete Operation
	 @DeleteMapping("/person/{id}")
	 public ResponseEntity<ElasticPerson> deletePersonUsingId(@PathVariable Long id)
	 {
		 try
		 {
		   ElasticPerson user= personService.getPerson(id);  
		    personService.deletePerson(id);
		   return new ResponseEntity<ElasticPerson>(user,HttpStatus.OK);      
	     }
		  catch(NoSuchElementException e)
		  {
			 return new ResponseEntity<ElasticPerson>(HttpStatus.NOT_FOUND);
		  }
	 }
}
