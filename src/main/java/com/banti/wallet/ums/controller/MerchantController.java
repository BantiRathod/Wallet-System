package com.banti.wallet.ums.controller;

import java.util.List;
import java.util.NoSuchElementException;

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
 
@PostMapping("/merchant/create")
public String createMerchantAccount(@RequestBody Merchant merchant )
  {
	 merchantService.createOrUpdate(merchant);
	 return " new merchant account has been created has heen created ";
  }
}
