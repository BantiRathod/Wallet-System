package com.banti.wallet.ums.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.enums.TxnType;
import com.banti.wallet.ums.model.Bank;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.pagination.PaginationRequest;
import com.banti.wallet.ums.service.BankService;

import com.banti.wallet.ums.service.TransactionService;
import com.banti.wallet.ums.service.PersonService;
import com.banti.wallet.ums.service.PersonWalletService;
import com.banti.wallet.ums.transactionClassesToPayment.TransactionRequest;
import com.banti.wallet.ums.transactionClassesToPayment.TransactionResponse;
import com.banti.wallet.ums.validator.business.TransactionBusinessValidator;
import com.banti.wallet.ums.validator.request.PaginationRequestValidator;
import com.banti.wallet.ums.validator.request.TransactionRequestValidator;

@RestController
public class TransactionController {
	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionBusinessValidator transactionBusinessValidator;
	
	//@Autowired
	//private PaginationRequestValidator paginationRequestValidator;
	@Autowired
	private PersonWalletService walletService;
	@Autowired
	private PersonService userService;

	@Autowired
	private BankService bankService;

	@GetMapping("transaction/summary")
	public ResponseEntity<Page<WalletTransaction>> getTrans(@RequestBody PaginationRequest paginationRequest) {
		logger.info("paginationRequest received {}", paginationRequest);
		try {
		//	paginationRequestValidator.summaryValidator(paginationRequest);
			Page<WalletTransaction> page = transactionService.listAll(paginationRequest);

			logger.info("paginationResponse respond {}", page);
			return new ResponseEntity<Page<WalletTransaction>>(page, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Page<WalletTransaction>>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Page<WalletTransaction>>(HttpStatus.NOT_FOUND);
		} 
	}

	@GetMapping("transaction/status/{id}")
	public ResponseEntity<String> getStatus(@PathVariable Long id) {
		try {
			String status = transactionService.getTransaction(id).getStatus();
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
		Person payerUser = (Person) p2mContext.get(ContextConstant.USER_ACCOUNT);
		Merchant payeeMerchant = (Merchant) p2mContext.get(ContextConstant.MERCHANT_ACCOUNT);

		transactionResponse.setTransactionType(TxnType.P2M.name());
		transactionResponse.setPayerName(payerUser.getFirstName());
		transactionResponse.setPayeeName(payeeMerchant.getShopName());
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(new Date());
		transactionResponse.setId(tempTransaction.getId());
		transactionResponse.setOrderId(request.getOrderId());
	}

	// API FOR SENDING MONEY TO A PERSON
	@PostMapping("/transaction/P2P")
	public ResponseEntity<TransactionResponse> payMoneyToPersion(@RequestBody TransactionRequest request) {
		logger.info("p2p transaction received {}", request);

		TransactionResponse transactionResponse = new TransactionResponse();

		try {

			TransactionRequestValidator.p2pRequestValidator(request);

			transactionBusinessValidator.p2pValidation(request);

			WalletTransaction transaction = doMoneyTransferToPerson(request);

			WalletTransaction tempTransaction = createTransactionP2P(request, transaction);

			generateP2PResponse(request, transactionResponse, tempTransaction);

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
			WalletTransaction tempTransaction) {
		Person payerUser = userService.findByMobileNo(request.getPayerMobileNo());
		Person payeeUser = userService.findByMobileNo(request.getPayeeMobileNo());

		transactionResponse.setPayerName(payerUser.getFirstName());
		transactionResponse.setPayeeName(payeeUser.getFirstName());
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(new Date());
		transactionResponse.setTransactionType(TxnType.P2P.name());
		transactionResponse.setId(tempTransaction.getId());
		transactionResponse.setOrderId(request.getOrderId());
	}

	private WalletTransaction createTransactionP2P(TransactionRequest request, WalletTransaction transaction) {
		transaction.setStatus("transaction succesful");
		transaction.setPayeeMobileNo(request.getPayeeMobileNo());
		transaction.setPayerMobileNo(request.getPayerMobileNo());
		transaction.setAmount(request.getAmount());
		transaction.setOrderId(request.getOrderId());
		transaction.setTransactionType(TxnType.P2P.name());
		transaction.setTransactionDate(new Date());
		WalletTransaction tempTransaction = transactionService.saveTransaction(transaction);
		return tempTransaction;
	}

	private WalletTransaction doMoneyTransferToPerson(TransactionRequest request) {

		PersonWallet payerUserWallet = walletService.get(request.getPayerMobileNo());

		Double balance = payerUserWallet.getBalance();
		Double amount = request.getAmount();

		WalletTransaction transaction = new WalletTransaction();

		balance -= amount;
		payerUserWallet.setBalance(balance);

		transaction.setPayerRemainingAmount(balance);

		PersonWallet payeeUserWallet = walletService.get(request.getPayeeMobileNo());

		balance = payeeUserWallet.getBalance();
		balance += amount;
		transaction.setPayeeRemainingAmount(balance);
		payeeUserWallet.setBalance(balance);

		walletService.update(payerUserWallet);
		walletService.update(payeeUserWallet);
		return transaction;
	}

	// API FOR ADDINGMONEY IN WALLET FROM BANK
	@PostMapping("transaction/addMoney")
	public ResponseEntity<TransactionResponse> addMoney(@RequestBody TransactionRequest request) {
		logger.info("addMoney transaction received {}", request);

		TransactionResponse transactionResponse = new TransactionResponse();
		Map<String, Object> addMoneyContext = new HashMap<>();

		try {
			TransactionRequestValidator.addMoneyRequestValidator(request);

			transactionBusinessValidator.addMoneyValidation(request, addMoneyContext);

			WalletTransaction transaction = doMoneyTransferFromBank(request, addMoneyContext);

			WalletTransaction tempTransaction = createTransactionForAddMoney(request, transaction);

			generateAddMoneyResponse(request, transactionResponse, addMoneyContext, tempTransaction);

		} catch (Exception e) {
			System.out.println("Exception occured" + e);
			transactionResponse.setOrderId(request.getOrderId());
			transactionResponse.setMessage(e.getMessage());
			transactionResponse.setDate(new Date());
			logger.info("addMoney transaction responded {}", transactionResponse);
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
		}
		logger.info("addMoney transaction transactionResponse {}", transactionResponse);
		return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	}

	private void generateAddMoneyResponse(TransactionRequest request, TransactionResponse transactionResponse,
			Map<String, Object> addMoneyContext, WalletTransaction tempTransaction) {
		Person user = (Person) addMoneyContext.get(ContextConstant.USER_ACCOUNT);

		transactionResponse.setPayerName(user.getFirstName());
		transactionResponse.setPayeeName(user.getFirstName());
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(new Date());
		transactionResponse.setTransactionType(TxnType.ADD_MONEY.name());
		transactionResponse.setId(tempTransaction.getId());
		transactionResponse.setOrderId(request.getOrderId());
	}

	private WalletTransaction createTransactionForAddMoney(TransactionRequest request, WalletTransaction transaction) {
		transaction.setStatus(" Money added succesfully");
		transaction.setOrderId(request.getOrderId());
		transaction.setPayeeMobileNo(request.getPayeeMobileNo());
		transaction.setPayerMobileNo(request.getPayerMobileNo());
		transaction.setAmount(request.getAmount());
		transaction.setTransactionType(TxnType.ADD_MONEY.name());
		transaction.setTransactionDate(new Date());
		WalletTransaction tempTransaction = transactionService.saveTransaction(transaction);
		return tempTransaction;
	}

	private WalletTransaction doMoneyTransferFromBank(TransactionRequest request, Map<String, Object> addMoneyContext) {

		Bank payerBank = (Bank) addMoneyContext.get(ContextConstant.BANK);

		Double balance = payerBank.getBalance();
		Double amount = request.getAmount();

		WalletTransaction transaction = new WalletTransaction();

		balance -= amount;
		payerBank.setBalance(balance);

		PersonWallet payeeUserWallet = (PersonWallet) addMoneyContext.get(ContextConstant.USER_WALLET);

		balance = payeeUserWallet.getBalance();
		balance += amount;
		transaction.setPayeeRemainingAmount(balance);
		payeeUserWallet.setBalance(balance);

		bankService.update(payerBank);
		walletService.update(payeeUserWallet);
		return transaction;
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