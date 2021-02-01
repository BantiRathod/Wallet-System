package com.banti.wallet.ums.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banti.wallet.ums.model.User;
import com.banti.wallet.ums.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;



@RestController                                                                    //work at server side and remove view part
public class UserController 
{
	Logger logger=LoggerFactory.getLogger(UserController.class);
	 @Autowired                                                                     //inject object of a class annotations    
	private UserService userService;                                                //injecting variable of service class(at service layer)
	 
     //RESTful API for getting all users
	 @GetMapping("/users")
	 public List<User> list()
	 {
	    return userService.listAll();
	 }
	 
	//RESTful API for getting the record particular user
	 @GetMapping("/user/get/{id}")
	 public ResponseEntity<User> get(@PathVariable Long id) {
	     try {
	         User user = userService.get(id);
	         return new ResponseEntity<User> (user, HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	     }      
	 }
	
	 //RESTful API for Create data
	 @PostMapping("/user")
	 public String add(@RequestBody User user) {
	     logger.info("request received to save the user {}",user);
		 user.setRegisterDate(new Date());
		 userService.save(user);
		 return "Mr. "+user.getUserName()+", you have registered successfully with "+user.getMobileNo()+" mobile number";
	 }
	 
	 
	// RESTful API for Update Operation
	 @PutMapping("/user/update/{id}")
	 public ResponseEntity<String> update(@RequestBody User user, @PathVariable Long id) {
	     try{
	         User existUser = userService.get(id);
	         existUser.setUserName(user.getUserName());
	         existUser.setEmail(user.getEmail());                          
	         existUser.setFirstName(user.getFirstName());                       
	         existUser.setLastName(user.getLastName());                          
	         existUser.setAddress(user.getAddress());
	         existUser.setStatus(user.getStatus());
	         existUser.setMobileNo(user.getMobileNo());
	         //existUser.setRegisterDate(user.getRegisterDate());
	         userService.save(existUser);
	         return new ResponseEntity<String>(" Record of the given id's user has been updated ",HttpStatus.OK);
	     } catch (NoSuchElementException e) {
	         return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	     }      
	 } 
	
	// RESTful API for Delete Operation
	 @DeleteMapping("/user/delete/{id}")
	 public ResponseEntity<User> delete(@PathVariable Long id)
	 {
		 try
		 {
		   User user= userService.get(id);
		   userService.delete(id);
		   return new ResponseEntity<User>(user,HttpStatus.OK);      
	     }
		  catch(NoSuchElementException e)
		  {
			 return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		  }
	 }
}
