package com.banti.wallet.ums.validator.business;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;
import com.banti.wallet.ums.elasticsearch.models.ElasticMerchantWallet;
import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;
import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;
import com.banti.wallet.ums.enums.AccountStatus;
import com.banti.wallet.ums.enums.PersonStatus;

import com.banti.wallet.ums.requestEntities.AddMoneyTransactionRequest;
import com.banti.wallet.ums.requestEntities.TransactionRequest;
import com.banti.wallet.ums.service.MerchantService;
import com.banti.wallet.ums.service.MerchantWalletService;
import com.banti.wallet.ums.service.PersonService;
import com.banti.wallet.ums.service.PersonWalletService;


@Service
public class TransactionBusinessValidator {
	
	@Autowired
	private PersonService personService;
	@Autowired 
	private MerchantService merchantService;
	@Autowired
	private MerchantWalletService merchantWalletService;
	@Autowired
	private PersonWalletService personWalletService;
	/*
	 * @Autowired private BankService bankService;
	 */

	
	//FOR P2M VALIDATION
	public void p2mValidation(TransactionRequest request, Map<String, Object> p2mContext) throws Exception {
		
		// CHECK USER EXIST OR NOT
		ElasticPerson person = personService.findByMobileNo(request.getPayerMobileNo());
		
		if(person==null) {
			throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());	
		}else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(person.getStatus())) {
			throw new Exception("user account is not active");
		}
		
		p2mContext.put(ContextConstant.PAYER_PERSON_ACCOUNT, person);
		
		// CHECK MERCHANT EXIST OR NOT
		ElasticMerchant merchant = merchantService.findByMobileNo(request.getPayeeMobileNo());
		
		if(merchant==null)  {
			 throw new Exception("merchant account not exist in system with mobile number "+request.getPayeeMobileNo());
		}else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(merchant.getStatus()))
			 throw new Exception("merchant account is not active in system with mobile number "+request.getPayeeMobileNo());
		
		p2mContext.put(ContextConstant.MERCHANT_ACCOUNT, merchant);
		
		//CHECK WALLET OF MERCAHNT AND USER 
		ElasticMerchantWallet payeeMerchantWallet = merchantWalletService.getMerchantWallet(request.getPayeeMobileNo());
		ElasticPersonWallet payerPersonWallet = personWalletService.getPersonWallet(request.getPayerMobileNo());
		
		if (AccountStatus.DISABLED.name().equalsIgnoreCase(payeeMerchantWallet.getStatus())) {
			     throw new Exception("merchant wallet is not active");
		}else if(AccountStatus.DISABLED.name().equalsIgnoreCase(payerPersonWallet.getStatus()))
			 throw new Exception("user wallet is not active");
		
		p2mContext.put(ContextConstant.MERCHANT_WALLET, payeeMerchantWallet);
		
		//CHECK USER HAS SUFFICIENT AMOUNT
		 payerPersonWallet = personWalletService.getPersonWallet(request.getPayerMobileNo());
		if(payerPersonWallet.getBalance()<request.getAmount()) {
			throw new Exception(String.format("user does not have sufficient balance, current balance: %f, request txnAmt: %f",payerPersonWallet.getBalance(),request.getAmount()));
		}		
		p2mContext.put(ContextConstant.PAYER_PERSON_WALLET, payerPersonWallet);
	}

	
	//FOR P2P VALIDATION
	public void p2pValidation(TransactionRequest request, Map<String, Object> p2pContext ) throws Exception {
		//CHECK PAYER USER EXIST 
		ElasticPerson payerPerson = personService.findByMobileNo(request.getPayerMobileNo());
		
		if(payerPerson==null) 
			throw new Exception("person account not exist in system with mobile number "+request.getPayerMobileNo());	
		else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(payerPerson.getStatus()))
			throw new Exception("person account is not active");
		
		p2pContext.put(ContextConstant.PAYER_PERSON_ACCOUNT,payerPerson);
		
		// CHECK payeeUser EXIST OR NOT
		ElasticPerson payeePerson = personService.findByMobileNo(request.getPayeeMobileNo());
		
		if(payeePerson==null)  
			 throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());
		else if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(payeePerson.getStatus()))
			 throw new Exception("user account is not active");
		
		p2pContext.put(ContextConstant.PAYEE_PERSON_ACCOUNT, payeePerson);
		
		//check user and merchant wallet is active or not
		ElasticPersonWallet payeePersonWallet = personWalletService.getPersonWallet(request.getPayeeMobileNo());
		ElasticPersonWallet payerPersonWallet = personWalletService.getPersonWallet(request.getPayerMobileNo());
		
		if (AccountStatus.DISABLED.name().equalsIgnoreCase(payeePersonWallet.getStatus())) 
			     throw new Exception("payee User wallet is not active");
		else if(AccountStatus.DISABLED.name().equalsIgnoreCase(payerPersonWallet.getStatus()))
			 throw new Exception("payer User wallet is not active");
		p2pContext.put(ContextConstant.PAYER_PERSON_WALLET, payerPersonWallet);
		
		//check user has sufficient amount
		if(payerPersonWallet.getBalance()<request.getAmount())
			throw new Exception(String.format("user does not have sufficient balance, current balance: %d, request txnAmt: %d",payerPersonWallet.getBalance(),request.getAmount()));
	
		p2pContext.put(ContextConstant.PAYEE_PERSON_WALLET, payeePersonWallet);
}

    //ADD MONEY BUSINESS VALIDATION
	public void addMoneyBusinessValidatoion(AddMoneyTransactionRequest request) throws Exception {
		
		ElasticPersonWallet personWallet = personWalletService.getPersonWallet(request.getMobileNo());
		
		if(personWallet==null)
			throw new Exception("wallet does not exist of this "+request.getMobileNo()+" mobile Number");
		else if(AccountStatus.DISABLED.name().equalsIgnoreCase(personWallet.getStatus()))
			throw new Exception("person wallet is not enable of "+request.getMobileNo()+" mobile Number");
		
	}		
	

	/*First Time MADE LIKE THAT
	 * // CHECK REQUEST PARAMETER FOR ADDMONEY API public void
	 * addMoneyValidation(TransactionRequest request, Map<String, Object>
	 * addMoneyContext) throws Exception { // check payer user exist or not
	 * 
	 * ElasticPerson person =
	 * personService.findByMobileNo(request.getPayerMobileNo()); if(person==null)
	 * throw new
	 * Exception("user account not exist in system with mobile number "+request.
	 * getPayerMobileNo()); else
	 * if(PersonStatus.UNACTIVE.name().equalsIgnoreCase(person.getStatus())) throw
	 * new Exception("user account is not active");
	 * 
	 * addMoneyContext.put(ContextConstant.PERSON_ACCOUNT,person);
	 * 
	 * 
	 * Bank bank = bankService.get(request.getPayerMobileNo()); if(bank==null) throw
	 * new Exception("bank not exist in system with account number "+
	 * request.getPayerMobileNo()); addMoneyContext.put(ContextConstant.BANK,bank);
	 * 
	 * 
	 * 
	 * //check user and merchant wallet is active or not ElasticPersonWallet
	 * payerUserWallet =
	 * personWalletService.getPersonWallet(request.getPayerMobileNo());
	 * if(AccountStatus.DISABLED.name().equalsIgnoreCase(payerUserWallet.getStatus()
	 * )) throw new Exception("payer User wallet is not active");
	 * addMoneyContext.put(ContextConstant.PERSON_WALLET, payerUserWallet);
	 * 
	 * //CHECK WHETHER USER HAS SUFFICIENT AMOUNT OR NOT
	 * if(payerUserWallet.getBalance()<request.getAmount()) throw new
	 * Exception(String.
	 * format("user does not have sufficient balance, current balance: %d, request txnAmt: %d"
	 * ,payerUserWallet.getBalance(),request.getAmount())); }
	 */
  
}
