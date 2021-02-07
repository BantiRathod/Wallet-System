package com.banti.wallet.ums.controller;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.service.MerchantService;


@RestController
public class MerchantController {

	Logger logger=LoggerFactory.getLogger(MerchantController.class);

@Autowired
private MerchantService merchantService;


@GetMapping("/merchants")
public ResponseEntity<List<Merchant>> listAll()
{
	try
	{
       List<Merchant> list=merchantService.getAll();
            return new ResponseEntity<List<Merchant>>(list,HttpStatus.OK);
	}
	catch(NoSuchElementException e)
	{
		return new ResponseEntity<List<Merchant>>(HttpStatus.NOT_FOUND);
	}	
}

@GetMapping("/merchant/{id}")
 public ResponseEntity<Merchant> fatchWallet(@PathVariable Long id)
 {
	
	try
	{
       Merchant merchant = merchantService.get(id);
         return new ResponseEntity<Merchant>(merchant,HttpStatus.OK);
	}
	catch(NoSuchElementException e)
	{
		return new ResponseEntity<Merchant>(HttpStatus.NOT_FOUND);
	}	
 }
 
@PostMapping("/merchantRegistration")
public String createMerchantAccount(@RequestBody Merchant merchant )
  {
	logger.info("merchant recieved {} ", merchant);
	 merchant.setRegisterDate(new Date());
	 merchantService.createOrUpdate(merchant);
	 return " New merchant account has been created, shopName is "+merchant.getShopName();
  }
}
