package com.banti.wallet.ums.validator.business;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.enums.AccountStatus;
import com.banti.wallet.ums.enums.PersonStatus;
import com.banti.wallet.ums.model.Bank;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.service.BankService;
import com.banti.wallet.ums.service.MerchantService;
import com.banti.wallet.ums.service.MerchantWalletService;
import com.banti.wallet.ums.service.PersonService;
import com.banti.wallet.ums.service.PersonWalletService;
import com.banti.wallet.ums.transactionClassesToPayment.TransactionRequest;


@Service
public class TransactionBusinessValidator {
	@Autowired
	private PersonService userService;
	@Autowired MerchantService merchantService;
	@Autowired
	private MerchantWalletService merchantWalletService;
	@Autowired
	private PersonWalletService userWalletService;
	@Autowired
	private BankService bankService;

	
	
	public void p2mValidation(TransactionRequest request, Map<String, Object> p2mContext) throws Exception {
		
		// CHECK USER EXIST OR NOT
		Person user = userService.findByMobileNo(request.getPayerMobileNo());
		
		if(user==null) {
			throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());	
		}else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(user.getStatus())) {
			throw new Exception("user account is not active");
		}
		
		p2mContext.put(ContextConstant.USER_ACCOUNT, user);
		
		// CHECK MERCHANT EXIST OR NOT
		Merchant merchant = merchantService.findByMobileNo(request.getPayeeMobileNo());
		
		if(merchant==null)  {
			 throw new Exception("merchant account not exist in system with mobile number "+request.getPayeeMobileNo());
		}else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(merchant.getStatus()))
			 throw new Exception("merchant account is not active in system with mobile number "+request.getPayeeMobileNo());
		
		p2mContext.put(ContextConstant.MERCHANT_ACCOUNT, merchant);
		
		//CHECK WALLET OF MERCAHNT AND USER 
		MerchantWallet payeeMerchantWallet = merchantWalletService.get(request.getPayeeMobileNo());
		PersonWallet payerUserWallet = userWalletService.get(request.getPayerMobileNo());
		
		if (AccountStatus.DISABLED.name().equalsIgnoreCase(payeeMerchantWallet.getStatus())) {
			     throw new Exception("merchant wallet is not active");
		}else if(AccountStatus.DISABLED.name().equalsIgnoreCase(payerUserWallet.getStatus()))
			 throw new Exception("user wallet is not active");
		p2mContext.put(ContextConstant.MERCHANT_WALLET, payeeMerchantWallet);
		
		//CHECK USER HAS SUFFICIENT AMOUNT
		 payerUserWallet = userWalletService.get(request.getPayerMobileNo());
		if(payerUserWallet.getBalance()<request.getAmount()) {
			throw new Exception(String.format("user does not have sufficient balance, current balance: %f, request txnAmt: %f",payerUserWallet.getBalance(),request.getAmount()));
		}		
		p2mContext.put(ContextConstant.USER_WALLET, payerUserWallet);
	}

	
	public void p2pValidation(TransactionRequest request) throws Exception {
		//CHECK PAYER USER EXIST 
		Person payerUser = userService.findByMobileNo(request.getPayerMobileNo());
		
		if(payerUser==null) 
			throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());	
		else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(payerUser.getStatus()))
			throw new Exception("user account is not active");
		
		//p2pContext.put(ContextConstant.USER_ACCOUNT,payerUser );
		
		// CHECK payeeUser EXIST OR NOT
		Person payeeUser = userService.findByMobileNo(request.getPayeeMobileNo());
		
		if(payeeUser==null)  
			 throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());
		else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(payeeUser.getStatus()))
			 throw new Exception("user account is not active");
		
		//p2pContext.put(ContextConstant.USER_ACCOUNT, payeeUser);
		
		//check user and merchant wallet is active or not
		PersonWallet payeeUsertWallet = userWalletService.get(request.getPayeeMobileNo());
		PersonWallet payerUserWallet = userWalletService.get(request.getPayerMobileNo());
		
		if (AccountStatus.DISABLED.name().equalsIgnoreCase(payeeUsertWallet.getStatus())) 
			     throw new Exception("payee User wallet is not active");
		else if(AccountStatus.DISABLED.name().equalsIgnoreCase(payerUserWallet.getStatus()))
			 throw new Exception("payer User wallet is not active");
		//p2pContext.put(ContextConstant.USER_WALLET, payeeUsertWallet);
		
		//check user has sufficient amount
		if(payerUserWallet.getBalance()<request.getAmount())
			throw new Exception(String.format("user does not have sufficient balance, current balance: %d, request txnAmt: %d",payerUserWallet.getBalance(),request.getAmount()));
	
		//p2pContext.put(ContextConstant.USER_WALLET, payerUserWallet);
}		
	

  // CHECK REQUEST PARAMETER FOR ADDMONEY API
  public void addMoneyValidation(TransactionRequest request, Map<String, Object> addMoneyContext) throws Exception
  {
	// check  payer user exist or not
	
	Person user = userService.findByMobileNo(request.getPayerMobileNo());
	if(user==null) 
		throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());	
	else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(user.getStatus()))
		throw new Exception("user account is not active");
	addMoneyContext.put(ContextConstant.USER_ACCOUNT, user);
	
	
	Bank bank = bankService.get(request.getPayerMobileNo());
	if(bank==null) 
		throw new Exception("bank not exist in system with account number "+ request.getPayerMobileNo());	
	addMoneyContext.put(ContextConstant.BANK,bank);
	
	

	//check user and merchant wallet is active or not
	PersonWallet payerUserWallet = userWalletService.get(request.getPayerMobileNo());
	 if(AccountStatus.DISABLED.name().equalsIgnoreCase(payerUserWallet.getStatus()))
		    throw new Exception("payer User wallet is not active");
    addMoneyContext.put(ContextConstant.USER_WALLET, payerUserWallet);
	
	//check user has sufficient amount
	if(payerUserWallet.getBalance()<request.getAmount()) 
		throw new Exception(String.format("user does not have sufficient balance, current balance: %d, request txnAmt: %d",payerUserWallet.getBalance(),request.getAmount()));		
	}
  
  
}
