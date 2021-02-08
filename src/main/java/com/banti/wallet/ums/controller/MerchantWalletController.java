package com.banti.wallet.ums.controller;

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

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchantWallet;
import com.banti.wallet.ums.requestEntities.MerchantWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantWalletRequest;
import com.banti.wallet.ums.service.MerchantWalletService;

@RestController
public class MerchantWalletController 
{
	Logger logger=LoggerFactory.getLogger(MerchantController.class);
 
	@Autowired
	private MerchantWalletService merchantWalletService;
	
	@GetMapping("/merchantWallets")
	public ResponseEntity<Iterable<ElasticMerchantWallet>> toGetListOfAllMerchantWallets()
	{
		try
		{
	         Iterable<ElasticMerchantWallet> list= merchantWalletService.getListOfAllMerchantWallet();
	         return new ResponseEntity<Iterable<ElasticMerchantWallet>>(list,HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			return new ResponseEntity<Iterable<ElasticMerchantWallet>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	@GetMapping("/merchantWallet/{mobileNo}")
	 public ResponseEntity<ElasticMerchantWallet> toGetMerchantWallet(@PathVariable String mobileNo)
	 {
		try
		{
			 ElasticMerchantWallet existWallet = merchantWalletService.getMerchantWallet(mobileNo);
	         return new ResponseEntity<ElasticMerchantWallet>(existWallet,HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			return new ResponseEntity<ElasticMerchantWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	@PostMapping("/merchantWallet")
	public String createWallet(@RequestBody MerchantWalletRequest merchantWallet)
      {
		logger.info("merchantWallet received {} ", merchantWallet);
		try
		{
		merchantWalletService.createMerchantWallet(merchantWallet);
		return "New merchant wallet of "+merchantWallet.getMobileNo()+" has heen created";
        }catch(Exception e)
		{
        	return "Exception occured, "+e.getMessage();
		}
	 }
	
	@PutMapping("/merchantWallet/{mobileNo}")
	public String toUpdateMerchantallet(@PathVariable String mobileNo, @RequestBody UpdateMerchantWalletRequest updateMerchantWalletRequest)
	{
		logger.info("UpdateMerchantWalletRequest received {} ",updateMerchantWalletRequest);
		try
		{
			merchantWalletService.updateMerchantWallet(updateMerchantWalletRequest, mobileNo);
			return "Merchant wallet has been updated successful ";
		}catch(Exception e)
		{
			return "Exception occured, "+e.getMessage();
		}	
    }
	
	
}