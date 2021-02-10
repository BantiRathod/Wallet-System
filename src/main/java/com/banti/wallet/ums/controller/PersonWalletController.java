package com.banti.wallet.ums.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;
import com.banti.wallet.ums.requestEntities.PersonWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdatePersonWalletRequest;
import com.banti.wallet.ums.service.PersonWalletService;
import com.banti.wallet.ums.validator.request.PersonWalletRequestBodyValidator;

@RestController
public class PersonWalletController {
	
	Logger logger = LoggerFactory.getLogger(PersonWalletController.class);
	
	@Autowired
	private PersonWalletService personWalletService;
	
	@Autowired
	PersonWalletRequestBodyValidator personWalletRequestBodyValidator;
	
	@GetMapping("/personWallets")
	public ResponseEntity<Iterable<ElasticPersonWallet>> listOfAllPersonWallet()
	{
		try
		{
	         Iterable<ElasticPersonWallet> list= personWalletService.getAllPersonWallet();
	         return new ResponseEntity<Iterable<ElasticPersonWallet>>(list,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<Iterable<ElasticPersonWallet>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	
	@GetMapping("/personWallet/{mobileNo}")
	 public ResponseEntity<ElasticPersonWallet> fatchWallet(@PathVariable String mobileNo)
	 {
		logger.info("mobileNo received from user {}", mobileNo);
		try
		{
			 personWalletRequestBodyValidator.personWalletMobileNoValidation(null , mobileNo);
	         ElasticPersonWallet existWallet = personWalletService.getPersonWallet(mobileNo);
	         logger.info("reponsed person Wallet {}", existWallet);
	         return new ResponseEntity<ElasticPersonWallet>(existWallet,HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<ElasticPersonWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	
	@PostMapping("/personWallet")
	public ResponseEntity<String> createWallet(@RequestBody PersonWalletRequest wallet)
      {
		logger.info("PersonWalletRequest received from user {}", wallet);
		  try
		 {
		   personWalletRequestBodyValidator.createPersonWalletValidation(wallet);
		   personWalletService.createPersonWallet(wallet);	
		   return new ResponseEntity<String>("New person wallet has heen created with "+ wallet.getMobileNo() +" mobileNo",HttpStatus.OK);
      }catch(Exception e){
    	  return new ResponseEntity<String>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}
     }	
	
	
	@PutMapping("/personWallet/{mobileNo}")
	public ResponseEntity<String> updatePersonWallet(@RequestBody UpdatePersonWalletRequest personWallet, @PathVariable String mobileNo)
	{
		try
		{
		  // TO VALIDATE NEW passed MOBILENO
		  personWalletRequestBodyValidator.personWalletMobileNoValidation(personWallet,mobileNo);
		  personWalletService.updatePersonWallet(personWallet, mobileNo);
		  return new ResponseEntity<String>(" person wallet updated successfully, by new mmobile no: "+personWallet.getMobileNo(),HttpStatus.OK);
	 }catch(Exception e)
		{
		  return new ResponseEntity<>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}
	}
	
	
	@DeleteMapping("/personWallet/{mobileNo}")
	public ResponseEntity<String> deletePersonWallet(@PathVariable String mobileNo)
	{
		try
		{
		  // TO VALIDATE NEW passed MOBILENO
		  personWalletRequestBodyValidator.personWalletMobileNoValidation(null,mobileNo);
		  
		  personWalletService.deletePersonWallet(mobileNo);
		  return new ResponseEntity<String>(" person wallet deletd successfully, of this mobile no: "+ mobileNo,HttpStatus.OK);
	 }catch(Exception e)
		{
		  return new ResponseEntity<>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}
	}
	
}
