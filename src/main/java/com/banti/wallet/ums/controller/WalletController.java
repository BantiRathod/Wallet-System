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

import com.banti.wallet.ums.model.Wallet;
import com.banti.wallet.ums.service.WalletService;

@RestController
public class WalletController {
	
	@Autowired
	private WalletService ws;
	
	@GetMapping("/wallets")
	public ResponseEntity<List<Wallet>> listAll()
	{
		try
		{
	       List<Wallet> list= ws.getAll();
	         return new ResponseEntity<List<Wallet>>(list,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<List<Wallet>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	@GetMapping("/wallet/{mobileNo}")
	 public ResponseEntity<Wallet> fatchWallet(@PathVariable String mobileNo)
	 {
		try
		{
	       Wallet existWallet = ws.get(mobileNo);
	         return new ResponseEntity<Wallet>(existWallet,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<Wallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	@PostMapping("/wallet")
	public void createWallet(@RequestBody Wallet wallet)
	{
		ws.create(wallet);
	}
	

}
