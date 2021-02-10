package com.banti.wallet.ums.controller;

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

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchantWallet;
import com.banti.wallet.ums.requestEntities.MerchantWalletRequest;
import com.banti.wallet.ums.requestEntities.UpdateMerchantWalletRequest;
import com.banti.wallet.ums.service.MerchantWalletService;
import com.banti.wallet.ums.validator.request.MerchantWalletRequestBodyValidator;

@RestController
public class MerchantWalletController 
{
	Logger logger=LoggerFactory.getLogger(MerchantController.class);
 
	@Autowired
	private MerchantWalletService merchantWalletService;
	
	@Autowired
	private MerchantWalletRequestBodyValidator  merchantWalletRequestBodyValidator;
	
	@GetMapping("/merchantWallets")
	public ResponseEntity<Iterable<ElasticMerchantWallet>> toGetListOfAllMerchantWallets()
	{
		try{
	         Iterable<ElasticMerchantWallet> list= merchantWalletService.getListOfAllMerchantWallet();
	         return new ResponseEntity<Iterable<ElasticMerchantWallet>>(list,HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("Exception occured"+e.getMessage());
			return new ResponseEntity<Iterable<ElasticMerchantWallet>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	
	@GetMapping("/merchantWallet/{mobileNo}")
	 public ResponseEntity<ElasticMerchantWallet> toGetMerchantWallet(@PathVariable String mobileNo)
	 {
		try{    //TO VALITED PASSED WALLET ID
			 merchantWalletRequestBodyValidator. merchantWalletIdValidatoion(mobileNo);
			 
			 ElasticMerchantWallet existWallet = merchantWalletService.getMerchantWallet(mobileNo);
			 logger.info("responsed merchant wallet {}",existWallet);
	         return new ResponseEntity<ElasticMerchantWallet>(existWallet,HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e.getMessage());
			return new ResponseEntity<ElasticMerchantWallet>(HttpStatus.NOT_FOUND);
		}	
	 }
	 
	
	@PostMapping("/merchantWallet")
	public ResponseEntity<String> createWallet(@RequestBody MerchantWalletRequest merchantWallet)
      {
		logger.info("merchantWallet received {} ", merchantWallet);
		try
		{
		   // TO VALIDED MOBILE NUMBER {ID IS MOBILE NUMBER THEREFORE, WE CAN USE ABOVE CALLED VALIDATION METHOD}
		   merchantWalletRequestBodyValidator. merchantWalletIdValidatoion(merchantWallet.getMobileNo());
		   
		   merchantWalletService.createMerchantWallet(merchantWallet);
		   return new ResponseEntity<String>("New merchant wallet of "+merchantWallet.getMobileNo()+"has heen created",HttpStatus.OK);
        }catch(Exception e)
		{
        	return new ResponseEntity<String>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}
	 }
	
	
	@PutMapping("/merchantWallet/{mobileNo}")
	public ResponseEntity<String> toUpdateMerchantallet(@PathVariable String mobileNo, @RequestBody UpdateMerchantWalletRequest updateMerchantWalletRequest)
	{
		logger.info("UpdateMerchantWalletRequest received {} ",updateMerchantWalletRequest);
		try
		{	//TO VALIDATE NEW MOBILE NUMBER AND EXIST NUMBER
			merchantWalletRequestBodyValidator.updateMerchantWalletValidation(mobileNo,updateMerchantWalletRequest);
			
			merchantWalletService.updateMerchantWallet(updateMerchantWalletRequest, mobileNo);
		    return new ResponseEntity<String>("merchant wallet of "+mobileNo+"has heen updated successfully",HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}	
    }
	
	
	@DeleteMapping("/merchantWallet/{mobileNo}")
	public ResponseEntity<String> todeleteMerchantallet(@PathVariable String mobileNo)
	{
		logger.info("mobile number received {} ", mobileNo);
		try
		{	//TO VALIDATE Id(MOBILE NUMBER)
			 merchantWalletRequestBodyValidator. merchantWalletIdValidatoion(mobileNo);
			
			merchantWalletService.deleteMerchantWallet(mobileNo);
		    return new ResponseEntity<String>("merchant wallet of "+mobileNo+"has heen updated successfully",HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>("Exception occured, "+e.getMessage(),HttpStatus.OK);
		}	
    }
	
}