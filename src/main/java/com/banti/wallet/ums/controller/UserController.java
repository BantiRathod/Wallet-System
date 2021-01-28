package com.banti.wallet.ums.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banti.wallet.ums.model.User;
import com.banti.wallet.ums.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;



@RestController                        //work at server side and remove view part
public class UserController 
{
	Logger logger=LoggerFactory.getLogger(UserController.class);
	 @Autowired                        //inject object of a class annotations    
	    private UserService service;   //injecting variable of service class(at service layer)
	 
	                                  //RESTful API for get Operation
	 @GetMapping("/users")
	 public List<User> list()
	 {
	    return service.listAll();
	 }
	 
	//RESTful API for retrieve Operation
	 @GetMapping("/users/{id}")
	 public ResponseEntity<User> get(@PathVariable Long id) {
	     try {
	         User user = service.get(id);
	         return new ResponseEntity<User> (user, HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	     }      
	 }
	
	 //RESTful API for Create Operation
	 @PostMapping("/users")
	 public void add(@RequestBody User user) {
	     logger.info("request received to save the user {}",user);
		 service.save(user);
	 }
	 
	// RESTful API for Update Operation
	 @PutMapping("/users/{id}")
	 public ResponseEntity<?> update(@RequestBody User user, @PathVariable Long id) {
	     try {
	         User existUser = service.get(id);
	         existUser.setUserName(user.getUserName());
	         existUser.setEmail(user.getEmail());                           //
	         existUser.setFname(user.getFname());                          // simply updating user field's by which we have sent
	         existUser.setLname(user.getLname());                         // 
	         existUser.setAdd1(user.getAdd1());
	         existUser.setAdd2(user.getAdd2());
	         existUser.setMobileNo(user.getMobileNo());
	         service.save(existUser);
	         return new ResponseEntity<>(HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	     }      
	 } 
	
	// RESTful API for Delete Operation
	 @DeleteMapping("/users/{id}")
	 public void delete(@PathVariable Long id) {
	       service.delete(id);
	 }
}
