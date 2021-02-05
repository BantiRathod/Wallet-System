package com.banti.wallet.ums.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.requestEntities.PersonRequestEntity;
import com.banti.wallet.ums.service.PersonService;

import java.util.List;
import java.util.NoSuchElementException;



@RestController                                                                    //work at server side and remove view part
public class PersonController 
{
	Logger logger=LoggerFactory.getLogger(PersonController.class);
	@Autowired                                                                     
	private PersonService personService;                                             

     //RESTful API for getting all users
	 @GetMapping("/listOfPerson")
	 public List<Person> list()
	 {
	    return personService.listAll();
	 }
	 
	//RESTful API for getting the record particular user
	 @GetMapping("/getPerson/{id}")
	 public ResponseEntity<Person> get(@PathVariable Long id) {
	     try {
	         Person person = personService.get(id);
	         return new ResponseEntity<Person> (person, HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
	     }      
	 }
	
	 //RESTful API for Create data
	 @PostMapping("/Sign-Up")
	 public ResponseEntity<String> registerPerson(@RequestBody PersonRequestEntity user) {
	     logger.info("request received to save User {}",user);
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
	 @PutMapping("/updatePerson/{id}")
	 public ResponseEntity<String> update(@RequestBody Person user, @PathVariable Long id) {
	     try
	     {
	    	 personService.updateUser(user);
	         return new ResponseEntity<String>(" Record of the given id's person has been updated ",HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	     }      
	 } 
	
	// RESTful API for Delete Operation
	 @DeleteMapping("/deletePerson/{id}")
	 public ResponseEntity<Person> delete(@PathVariable Long id)
	 {
		 try
		 {
		   Person user= personService.get(id);
		   personService.delete(id);
		   return new ResponseEntity<Person>(user,HttpStatus.OK);      
	     }
		  catch(NoSuchElementException e)
		  {
			 return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		  }
	 }
}
