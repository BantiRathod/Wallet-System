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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;
import com.banti.wallet.ums.requestEntities.MerchantRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantRequest;
import com.banti.wallet.ums.service.MerchantService;


@RestController
public class MerchantController {

	Logger logger=LoggerFactory.getLogger(MerchantController.class);

@Autowired
private MerchantService merchantService;


@GetMapping("/merchants")
public ResponseEntity<Iterable<ElasticMerchant>> toGetlistOfAllMerchants()
{
	try
	{
       Iterable<ElasticMerchant> list=merchantService.getListOfAllMerchants();
            return new ResponseEntity<Iterable<ElasticMerchant>>(list,HttpStatus.OK);
	}
	catch(NoSuchElementException e)
	{
		return new ResponseEntity<Iterable<ElasticMerchant>>(HttpStatus.NOT_FOUND);
	}	
}

@GetMapping("/merchant/{id}")
 public ResponseEntity< ElasticMerchant > fatchWallet(@PathVariable Long id)
 {
	try
	{
         ElasticMerchant merchant = merchantService.getMerchant(id);
         return new ResponseEntity< ElasticMerchant >(merchant,HttpStatus.OK);
	}
	catch(Exception e)
	{
		logger.error(e.getMessage());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}	
 }
 
    @PostMapping("/merchant")
    public String toMerchantRegistration(@RequestBody MerchantRequest merchant )
  {
	 logger.info("merchantRequest  recieved {} ", merchant);
	 try
	 {
	 merchantService.createMerchantAccount(merchant);
	 return " New merchant account has been created, shopName is "+merchant.getShopName();
	 }catch(Exception e)
	 {
		return "Exception occured "+ e.getMessage();
	 }
  }

@PutMapping("/merchant/{id}")
public String toUpdateMerchantAccount(@RequestBody 	UpdateMerchantRequest merchant ,@ PathVariable Long id)
{
	try
	{
		merchantService.updateMerchantAccount(merchant, id);
		return " Merchant record has been updated";
	}catch(Exception e)
	{
		return "run time exception occured "+e.getMessage();
	}
	
}

}
