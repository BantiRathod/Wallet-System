package com.banti.wallet.ums.controller;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.service.PersonWalletService;

@RestController
public class PersonWalletController {
	
	@Autowired
	private PersonWalletService walletService;
	
	@GetMapping("/wallets")
	public ResponseEntity<List<PersonWallet>> listAll()
	{
		try
		{
	       List<PersonWallet> list= walletService.getAll();
	         return new ResponseEntity<List<PersonWallet>>(list,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<List<PersonWallet>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	@GetMapping("/wallet/get/{mobileNo}")
	 public ResponseEntity<PersonWallet> fatchWallet(@PathVariable String mobileNo)
	 {
		try
		{
	       PersonWallet existWallet = walletService.get(mobileNo);
	         return new ResponseEntity<PersonWallet>(existWallet,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<PersonWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	@PostMapping("/wallet")
	public String createWallet(@RequestBody PersonWallet wallet)
      {
		wallet.setCreatedDate(new Date());
		walletService.create(wallet);
		return "new user wallet of "+wallet.getMobileNo()+" on "+ wallet.getCreatedDate() +" has heen created";
      }
	
	/*@PutMapping("/wallet/status/{mobileNo}")
	public ResponseEntity<Wallet> update(@PathVariable String mobileNo, @RequestBody )
	{
		try
		{
		 walletService.get(mobileNo).setStatus(status);
		 
		 return new ResponseEntity<Wallet>(,HttpStatus.OK);
	}*/

}
