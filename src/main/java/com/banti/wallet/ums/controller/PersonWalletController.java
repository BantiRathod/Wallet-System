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
	
	/**
	  * THIS API IS REPONSIBLE FOR RETREIVING ALL REGISTERED PERSON'S WALLETS.
	  * 
	  * HERE, METHOD OF PERSON SERVICE LAYER WOULD BE INVOKED IN ORDER TO GET PERSON RECORDS....
	  * AND PERSON RECORDS WOULD BE RETRIEVED FROM ELASTICSEARCH DATA BASE INSTEAD OF MYSQL.  
	  * 
	  * IF RECORDS NOT EXIST THEN RETURN HTTPSTATUS "NOT_FOUND".
	  * OK        :- GOT RECORDS
	  * NOT_FOUND :- DID NOT GOT RECORDS  
	  *   
	 * @return
	 */
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
	
	/**
	 *  THIS API IS REPONSIBLE FOR RETREIVING A WALLET OF REGISTERED PERSON.
	 *  
	 * @param mobileNo IS PARAMETER PASSED WHILE MAKING REQUEST BY USER.
	 * 
	 * personWalletMobileNoValidation METHOD WILL VALIDATE PASSED MOBILE NUMBER IF IT IS VALID THEN INVOKE METHOD OF...
	 * SERVICE LAYER CLASS FOR RETRIEVING PERSON WALLET  OTHERWISE THROW AN EXCEPTION.  
	 * 
	 * @return PERSON WALLET WOULD BE RETURN IF EXIST, ELSE RETURN HTTPSTATUS.
	 * OK        :- GOT PERSON WALLET
	 * NOT_FOUND :- DIDN'T GOT PERSIN WALLET 
	 */
	
	@GetMapping("/personWallet/{mobileNo}")
	 public ResponseEntity<ElasticPersonWallet> fatchWallet(@PathVariable String mobileNo)
	 {
		logger.info("mobileNo received from user {}", mobileNo);
		try
		{   
			//FOR MOBILE NUMBER VALIDATION
			 personWalletRequestBodyValidator.personWalletMobileNoValidation(null , mobileNo);
			 
	         ElasticPersonWallet existWallet = personWalletService.getPersonWallet(mobileNo);
	         logger.info("reponsed person Wallet {}", existWallet);
	         return new ResponseEntity<ElasticPersonWallet>(existWallet,HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<ElasticPersonWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
    /**
     * THIS API IS REPONSIBLE FOR CREATING A PERSON WALLET OF REGISTERED PERSON.
	 *  
	 * @param WALLET IS PARAMETER TYPE OF PersonWalletRequest PASSED WHILE MAKING REQUEST BY USER.
	 * FIELDS OR VARIABLES{ balance, mobileNo }
	 * 
	 * createPersonWalletValidation METHOD WILL VALIDATE PASSED WALLET IF IT IS VALID THEN INVOKE METHOD OF...
	 * SERVICE LAYER CLASS FOR CREATING PERSON WALLET OTHERWISE THROW AN EXCEPTION.  
	 * 
	 * @return STRING WOULD BE RETURN IF SUCCESSFULLY CREATED, ELSE RETURN EXCEPTION'S MESSAGE.   
     */
	@PostMapping("/personWallet")
	public ResponseEntity<String> createWallet(@RequestBody PersonWalletRequest wallet)
      {
		logger.info("PersonWalletRequest received from user {}", wallet);
		  try
		 {
		   //TO CHECK PASSED WALLET VALID OR NOT
		   personWalletRequestBodyValidator.createPersonWalletValidation(wallet);
		   
		   personWalletService.createPersonWallet(wallet);	
		   return new ResponseEntity<String>("New person wallet has heen created for "+ wallet.getMobileNo() +" mobileNo",HttpStatus.OK);
      }catch(Exception e){
    	  return new ResponseEntity<String>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}
     }	
	
	
	/**
     * THIS API IS REPONSIBLE FOR UPDATING A PERSON WALLET OF REGISTERED PERSON.
	 *  
	 * @param personWallet AND mobileNo ARE PARAMETERS TYPE OF UpdatePersonWalletRequest AND String RESPECTIVLY PASSED WHILE MAKING REQUEST BY USER.
	 * FIELDS OR VARIABLES OF UpdatePersonWalletRequest{ mobileNo }
	 * 
	 * personWalletMobileNoValidation METHOD WILL VALIDATE PASSED personWallet and string IF THEY ARE VALID THEN INVOKE METHOD OF...
	 * SERVICE LAYER CLASS FOR UPDATING PERSON WALLET OTHERWISE THROW AN EXCEPTION.  
	 * 
	 * @return STRING WOULD BE RETURN IF SUCCESSFULLY UPDATED, ELSE RETURN EXCEPTION'S MESSAGE.   
     */
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
	

	/**
     * THIS API IS REPONSIBLE FOR DELETING A PERSON WALLET OF REGISTERED PERSON.
	 *  
	 * @param mobileNo IS A PARAMETER SENT BY USER WHILE MAKING REQUEST.
	 * 
	 * personWalletMobileNoValidation METHOD WILL VALIDATE PASSED mobileNo IF IT IS VALID THEN INVOKE METHOD OF...
	 * SERVICE LAYER CLASS FOR DELETING PERSON WALLET OTHERWISE THROW AN EXCEPTION.  
	 * 
	 * @return STRING WOULD BE RETURN IF SUCCESSFULLY DELETED, ELSE RETURN EXCEPTION'S MESSAGE.   
     */
	@DeleteMapping("/personWallet/{mobileNo}")
	public ResponseEntity<String> deletePersonWallet(@PathVariable String mobileNo)
	{
		try
		{
		  // TO VALIDATE NEW passed MOBILENO
		  personWalletRequestBodyValidator.personWalletMobileNoValidation(null,mobileNo);
		  
		  personWalletService.deletePersonWallet(mobileNo);
		  return new ResponseEntity<String>(" person wallet deletd successfully, of this mobile no: "+ mobileNo,HttpStatus.OK);
	 }catch(Exception e){
		  return new ResponseEntity<>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}
	}
	
}
