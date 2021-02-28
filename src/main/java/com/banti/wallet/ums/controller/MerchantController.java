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

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;
import com.banti.wallet.ums.requestEntities.MerchantRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantRequest;
import com.banti.wallet.ums.service.MerchantService;
import com.banti.wallet.ums.validator.request.MerchantRequestBodyValidator;


@RestController
public class MerchantController {

Logger logger=LoggerFactory.getLogger(MerchantController.class);

@Autowired
private MerchantService merchantService;

@Autowired
private MerchantRequestBodyValidator merchantRequestBodyValidator;

@GetMapping("/merchants")
public ResponseEntity<Iterable<ElasticMerchant>> toGetlistOfAllMerchants()
{
	try
	{
            Iterable<ElasticMerchant> list=merchantService.getListOfAllMerchants();
            logger.info("responsed merchants record, {}",list);
            return new ResponseEntity<Iterable<ElasticMerchant>>(list,HttpStatus.OK);
	}
	catch(NoSuchElementException e)
	{
		  logger.error("Exception occured {}", e.getMessage());
		  return new ResponseEntity<Iterable<ElasticMerchant>>(HttpStatus.NOT_FOUND);
	}	
}

@GetMapping("/merchant/{id}")
 public ResponseEntity< ElasticMerchant > getMerchantUsingId(@PathVariable Long id)
 {
	try
	{
		 merchantRequestBodyValidator.merchantRequestIdValidation(id);
         ElasticMerchant merchant = merchantService.getMerchant(id);
         logger.info("responsed merchant record {} "+ merchant);
         return new ResponseEntity< ElasticMerchant >(merchant,HttpStatus.OK);
	}
	catch(Exception e)
	{
		logger.error("Exception occured, "+e.getMessage());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}	
 }
 
    @PostMapping("/merchant")
    public ResponseEntity<String> toMerchantRegistration(@RequestBody MerchantRequest merchant )
  {
	 logger.info("merchantRequest  recieved {} ", merchant);
	 try
	 {
		 //TO VALITEDE THE REQUEST BODY
		  merchantRequestBodyValidator.createMerchantValidation(merchant);
	      merchantService.createMerchantAccount(merchant);
	      return new ResponseEntity<String>(" New merchant account has been created, shopName is "+merchant.getShopName(),HttpStatus.OK);
	 }catch(Exception e)
	 {
		return new ResponseEntity<String>("Exception occured "+ e.getMessage(),HttpStatus.OK);
	 }
  }

@PutMapping("/merchant/{id}")
public ResponseEntity<String> toUpdateMerchantAccount(@RequestBody 	UpdateMerchantRequest merchant ,@ PathVariable Long id)
{
	try
	{
		merchantService.updateMerchantAccount(merchant, id);
		return new ResponseEntity<String>("Merchant record has been updated",HttpStatus.OK);
	}catch(Exception e)
	{
		return new ResponseEntity<String>("Exception occured "+ e.getMessage(),HttpStatus.OK);
	}	
}

@DeleteMapping("/merchant/{id}")
public ResponseEntity<String> toDeleteExistMerchantAccount(@PathVariable Long id)
{
	try
	{
		//TO REQUEST BODY VALIDATOR
		 merchantRequestBodyValidator.merchantRequestIdValidation(id);
		 merchantService.deleteMerchantAccount(id); 
		 return new ResponseEntity<String>("Merchant account deleted successfully of id ="+id,HttpStatus.OK);
	}catch(Exception e)
	{
	     return new ResponseEntity<String>("Exception occured, "+e.getMessage(),HttpStatus.OK);
	}
}
}
