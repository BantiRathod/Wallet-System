package com.banti.wallet.ums.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;
import com.banti.wallet.ums.elasticsearch.models.ElasticWalletTransaction;
import com.banti.wallet.ums.enums.TxnType;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.requestEntities.AddMoneyTransactionRequest;
import com.banti.wallet.ums.requestEntities.PaginationRequest;
import com.banti.wallet.ums.requestEntities.TransactionRequest;
import com.banti.wallet.ums.responseEntities.TransactionResponse;
import com.banti.wallet.ums.service.PersonService;
import com.banti.wallet.ums.service.TransactionService;
import com.banti.wallet.ums.validator.request.PaginationRequestValidator;
import com.banti.wallet.ums.validator.request.TransactionRequestValidator;

@RestController
public class TransactionController {
	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private PaginationRequestValidator paginationRequestValidator;

	@Autowired 
	private PersonService personService;
	 

	@GetMapping("/transactionSummary")
	public ResponseEntity<Iterable<ElasticWalletTransaction>> getTransactionSummary(@RequestBody PaginationRequest paginationRequest) {
		logger.info("paginationRequest received {}", paginationRequest);
		
		try {
			paginationRequestValidator.paginationRequestValidation(paginationRequest);

			Iterable<ElasticWalletTransaction> page = transactionService.getListOfAllTransaction(paginationRequest);
			
			logger.info("paginationResponse respond {}", page);
			return new ResponseEntity<Iterable<ElasticWalletTransaction>>(page, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Iterable<ElasticWalletTransaction>>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Iterable<ElasticWalletTransaction>>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/transactionStatus/{id}")
	public ResponseEntity<String> getTransactionStatus(@PathVariable Long id) {
		try {
			String status = transactionService.getStatus(id);
			return new ResponseEntity<String>(status, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	// API FOR SENDING MONEY TO A MERCHANT
	@PostMapping("/transaction/P2M")
	public ResponseEntity<TransactionResponse> payMoneyToMerchant(@RequestBody TransactionRequest request) {
		logger.info("p2m transaction received {}", request);

		TransactionResponse transactionResponse = new TransactionResponse();
		Map<String, Object> p2mContext = new HashMap<>();

		try {
			TransactionRequestValidator.p2mRequestValidator(request);
			WalletTransaction tempTransaction = transactionService.performP2M(request, p2mContext);
			logger.info("after performing transaction p2mContext {}",p2mContext);
			generateP2MResponse(request, transactionResponse, p2mContext, tempTransaction);

		} catch (Exception e) {
			logger.error("Exception occured while doing p2m", e);
			transactionResponse.setOrderId(request.getOrderId());
			transactionResponse.setMessage(e.getMessage());
			transactionResponse.setDate(new Date());
		}
		logger.info("p2m transaction transactionResponse {}", transactionResponse);
		return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	}

	private void generateP2MResponse(TransactionRequest request, TransactionResponse transactionResponse,
			Map<String, Object> p2mContext, WalletTransaction tempTransaction) {

		Person payerUser = (Person) p2mContext.get(ContextConstant.PAYER_PERSON_ACCOUNT);
		Merchant payeeMerchant = (Merchant) p2mContext.get(ContextConstant.MERCHANT_ACCOUNT);

		transactionResponse.setTransactionType(TxnType.P2M.name());
		transactionResponse.setPayerName(payerUser.getFirstName());
		transactionResponse.setPayeeName(payeeMerchant.getShopName());
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(tempTransaction.getTransactionDate());
		transactionResponse.setId(tempTransaction.getId());
		transactionResponse.setOrderId(request.getOrderId());
	}

	// API FOR SENDING MONEY TO A PERSON
	@PostMapping("/transaction/P2P")
	public ResponseEntity<TransactionResponse> payMoneyToPersion(@RequestBody TransactionRequest request) {
		logger.info("p2p transaction received {}", request);

		TransactionResponse transactionResponse = new TransactionResponse();
		Map<String, Object> p2pContext = new HashMap<>();
		try {

			TransactionRequestValidator.p2pRequestValidator(request);
			logger.info("after performing transaction p2pContext {}",p2pContext);
			WalletTransaction tempTransaction = transactionService.performp2p(request, p2pContext);
			
			generateP2PResponse(request, transactionResponse, tempTransaction, p2pContext);

		} catch (Exception e) {
			System.out.println("Exception occured" + e);
			transactionResponse.setOrderId(request.getOrderId());
			transactionResponse.setMessage(e.getMessage());
			transactionResponse.setDate(new Date());
			logger.info("p2m transaction transactionResponse {}", transactionResponse);
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
		}
		logger.info("p2m transaction transactionResponse {}", transactionResponse);
		return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	}

	private void generateP2PResponse(TransactionRequest request, TransactionResponse transactionResponse,
			WalletTransaction tempTransaction, Map<String, Object> p2pContext) {

		Person payerPerson = (Person) p2pContext.get(ContextConstant.PAYER_PERSON_ACCOUNT);
		Person payeePerson = (Person) p2pContext.get(ContextConstant.PAYEE_PERSON_ACCOUNT);

		transactionResponse.setPayerName(payerPerson.getFirstName());
		transactionResponse.setPayeeName(payeePerson.getFirstName());
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(new Date());
		transactionResponse.setTransactionType(TxnType.P2P.name());
		transactionResponse.setId(tempTransaction.getId());
		transactionResponse.setOrderId(request.getOrderId());
	}

	
	  //ADD M0NEY API  
	  @PostMapping("/transaction/addMoney") 
	  public ResponseEntity<TransactionResponse> addMoney(@RequestBody AddMoneyTransactionRequest request) 
	  { 
	   logger.info("addMoney transaction received request Body {}", request);
	   TransactionResponse transactionResponse = new TransactionResponse();
	  try {
		   TransactionRequestValidator.addMoneyRequestValidator(request);
	 
	       WalletTransaction transaction=transactionService.performAddMoney(request);
	   
	       generateAddMoneyResponse(request, transactionResponse, transaction);
	  
	  } catch (Exception e)
	  { 
	  System.out.println("Exception occured" + e);
	  transactionResponse.setOrderId("NA");
	  transactionResponse.setMessage(e.getMessage());
	  transactionResponse.setDate(new Date());
	  logger.info("addMoney transaction responded {}", transactionResponse); 
	  return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	  } 
	  logger.info("addMoney transaction transactionResponse {}",transactionResponse); 
	  return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	  }
	  
	  private void generateAddMoneyResponse(AddMoneyTransactionRequest request,TransactionResponse transactionResponse, WalletTransaction Transaction) { 
	 
	  ElasticPerson person = personService.findByMobileNo(request.getMobileNo());
	  
	  transactionResponse.setPayerName("External source");
	  transactionResponse.setPayeeName(person.getFirstName());
	  transactionResponse.setMessage("Transaction Successful");
	  transactionResponse.setDate(new Date());
	  transactionResponse.setTransactionType(TxnType.ADD_MONEY.name());
	  transactionResponse.setId(Transaction.getId());
	  transactionResponse.setOrderId("not avialable");
	  
	  }
	  
}

/*
 * User payerUser = userService.findByMobileNo(request.getPayerMobileNo()); User
 * payeeUser = userService.findByMobileNo(request.getPayeeMobileNo());
 * 
 * TransactionResponse transactionResponse = new TransactionResponse();
 * 
 * if (payerUser == null || payeeUser == null) { transactionResponse.
 * setMessage("Transaction couldn't be done : user is not exist");
 * transactionResponse.setDate(new Date()); return new
 * ResponseEntity<TransactionResponse>(transactionResponse,
 * HttpStatus.NOT_FOUND); }
 * 
 * transactionResponse.setPayeeName(payeeUser.getFname());
 * transactionResponse.setPayerName(payerUser.getFname());
 * 
 * if (payerUser.getStatus() == "unactive" || payeeUser.getStatus() ==
 * "unactive") // user is not active { transactionResponse.
 * setMessage("Transaction couldn't be done : user is not active");
 * transactionResponse.setDate(new Date()); return new
 * ResponseEntity<TransactionResponse>(transactionResponse,
 * HttpStatus.NOT_FOUND); }
 * 
 * Wallet payerWallet = walletService.get(ptopTransaction.getPayerMobileNo());
 * // wallet is active or not Wallet payeeWallet =
 * walletService.get(ptopTransaction.getPayeeMobileNo());
 * 
 * if (payerWallet.getStatus() == "disable" || payeeWallet.getStatus() ==
 * "disable") { transactionResponse.
 * setMessage("Transaction couldn't be done : wallet is not active");
 * transactionResponse.setDate(new Date()); return new
 * ResponseEntity<TransactionResponse>(transactionResponse,
 * HttpStatus.NOT_FOUND); }
 * 
 * Double balance = payerWallet.getBalance(); Double amount =
 * ptopTransaction.getAmount();
 * 
 * WalletTransaction transaction = new WalletTransaction();
 * 
 * if ((amount >= 0) && (balance >= amount)) { balance -= amount;
 * 
 * payerWallet.setBalance(balance);
 * 
 * transaction.setPayerRemainingAmount(balance); balance =
 * payeeWallet.getBalance(); balance += amount;
 * transaction.setPayeeRemainingAmount(balance);
 * payeeWallet.setBalance(balance);
 * 
 * walletService.update(payerWallet); walletService.update(payeeWallet);
 * 
 * transaction.setStatus("transaction succesful"); } else { transaction.
 * setStatus(" transaction failed : balance is not sufficient to pay");
 * transaction.setPayeeRemainingAmount(payeeWallet.getBalance());
 * transaction.setPayerRemainingAmount(payerWallet.getBalance()); }
 * transaction.setPayeeMobileNo(ptopTransaction.getPayeeMobileNo());
 * transaction.setPayerMobileNo(ptopTransaction.getPayerMobileNo());
 * transaction.setAmount(ptopTransaction.getAmount());
 * transaction.setTransactionType(TxnType.P2P.name());
 * transaction.setTransactionDate(new Date());
 * 
 * WalletTransaction currentTransaction =
 * transactionService.createTransaction(transaction);
 * 
 * transactionResponse.setMessage("Transaction Successful");
 * transactionResponse.setDate(currentTransaction.getTransactionDate());
 * transactionResponse.setId(currentTransaction.getId());
 * transactionResponse.setOrderId(ptopTransaction.getOrderId()); return new
 * ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
 */