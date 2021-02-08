package com.banti.wallet.ums.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.requestEntities.PersonWalletRequest;
import com.banti.wallet.ums.service.PersonWalletService;
import com.banti.wallet.ums.validator.request.PersonWalletRequestValidator;

@RestController
public class PersonWalletController {
	Logger logger = LoggerFactory.getLogger(PersonWalletController.class);
	@Autowired
	private PersonWalletService personWalletService;
	
	@Autowired
	PersonWalletRequestValidator personWalletRequestValidator;
	
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
	       ElasticPersonWallet existWallet = personWalletService.getPersonWallet(mobileNo);
	         return new ResponseEntity<ElasticPersonWallet>(existWallet,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<ElasticPersonWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	@PostMapping("/personWallet")
	public String createWallet(@RequestBody PersonWalletRequest wallet)
      {
		logger.info("PersonWalletRequest received from user {}", wallet);
		  try
		 {
		   personWalletRequestValidator.personWalletRequestValidation(wallet);
		   personWalletService.createPersonWallet(wallet);	
		   return "new person wallet has heen created with  "+ wallet.getMobileNo()+" mobileNo";
      }catch(Exception e)
		{
    	  return e.getMessage();
		}
     }	
	
	@PutMapping("/personWallet/{mobileNo}")
	public ResponseEntity<String> updatePersonWallet(@RequestBody PersonWallet personWallet, @PathVariable String mobileNo)
	{
		try
		{
		 personWalletService.updatePersonWallet(personWallet);
		 return new ResponseEntity<String>("person wallet updated successfully, by new mmobile no: "+personWallet.getMobileNo(),HttpStatus.OK);
	 }catch(Exception e)
		{
		  return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
		}
	}
}
