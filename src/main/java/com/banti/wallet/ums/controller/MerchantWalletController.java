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
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.service.MerchantWalletService;

@RestController
public class MerchantWalletController 
{
 
	@Autowired
	private MerchantWalletService merchantWalletService;
	
	@GetMapping("/merchantWallets")
	public ResponseEntity<List<MerchantWallet>> listAll()
	{
		try
		{
	         List<MerchantWallet> list= merchantWalletService.getAll();
	         return new ResponseEntity<List<MerchantWallet>>(list,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<List<MerchantWallet>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	@GetMapping("/merchantWallet/get/{mobileNo}")
	 public ResponseEntity<MerchantWallet> fatchWallet(@PathVariable String mobileNo)
	 {
		try
		{
			MerchantWallet existWallet = merchantWalletService.get(mobileNo);
	         return new ResponseEntity<MerchantWallet>(existWallet,HttpStatus.OK);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<MerchantWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	@PostMapping("/wallet")
	public String createWallet(@RequestBody MerchantWallet merchantWallet)
      {
		merchantWalletService.createOrUpdate(merchantWallet);
		return "new merchant wallet of "+merchantWallet.getMobileNo()+" on "+merchantWallet.getMerchantWalletcreatedDate()+"has heen created";
      }
	
}
