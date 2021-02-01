package com.banti.wallet.ums.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import javax.print.attribute.HashAttributeSet;

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
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.User;
import com.banti.wallet.ums.model.Wallet;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.service.BankService;
import com.banti.wallet.ums.service.MerchantWalletService;
import com.banti.wallet.ums.service.TransactionService;
import com.banti.wallet.ums.service.UserService;
import com.banti.wallet.ums.service.WalletService;
import com.banti.wallet.ums.validator.business.TransactionBusinessValidator;
import com.banti.wallet.ums.validator.request.TransactionRequestValidator;

@RestController
public class TransactionController {
	Logger logger=LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionBusinessValidator transactionBusinessValidator;

	@Autowired
	private WalletService walletService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private BankService bankService;
	//@Autowired
	//private MerchantRepository merchantRepository;
	@Autowired
	private MerchantWalletService merchantWalletService;

	/*
	 * request validation negative amount user exist or not make separate request
	 * and response object for each API status in wallet, enable/disable/freeze,
	 * remaining balance after each transaction created and date
	 * 
	 */

	/*
	 * TODO add paging in request parameter
	 */

	@GetMapping("transaction/summary/{id}")
	public ResponseEntity<Page<WalletTransaction>> getTrans(@PathVariable Long id) {
		try // UPDATE static to dynamic page no;
		{
			String payerNo = userService.get(id).getMobileNo();
			Page<WalletTransaction> page = transactionService.listAll(1, payerNo);
			return new ResponseEntity<Page<WalletTransaction>>(page, HttpStatus.OK);
		} catch (NoSuchElementException e) {
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
		TransactionResponse transactionResponse = new TransactionResponse();
		Map<String, Object> p2mContext = new HashMap<>();
		try {
			TransactionRequestValidator.p2mRequestValidator(request);
			//call service
			transactionBusinessValidator.p2mValidation(request, p2mContext);

			WalletTransaction transaction = doMoneyTransfer(request, p2mContext);

			WalletTransaction tempTransaction = createTransaction(request, transaction);
			//end service
			generateP2MResponse(request, transactionResponse, p2mContext, tempTransaction);

		} catch (Exception e) {
			System.out.println("Exception occured" + e);
			transactionResponse.setOrderId(request.getOrderId());
			transactionResponse.setMessage(e.getMessage());
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
		}
		return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	}

	private void generateP2MResponse(TransactionRequest request, TransactionResponse transactionResponse,
			Map<String, Object> p2mContext, WalletTransaction tempTransaction) {
		User payerUser = (User) p2mContext.get(ContextConstant.USER_ACCOUNT);
		Merchant payeeMerchant = (Merchant) p2mContext.get(ContextConstant.MERCHANT_ACCOUNT);

		transactionResponse.setPayerName(payerUser.getFname());
		transactionResponse.setPayeeName(payeeMerchant.getShopName());
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(tempTransaction.getTransactionDate());
		transactionResponse.setId(tempTransaction.getId());
		transactionResponse.setOrderId(request.getOrderId());
	}

	private WalletTransaction createTransaction(TransactionRequest request, WalletTransaction transaction) {
		transaction.setStatus("transaction succesful");
		transaction.setPayeeMobileNo(request.getPayeeMobileNo());
		transaction.setPayerMobileNo(request.getPayerMobileNo());
		transaction.setAmount(request.getAmount());
		transaction.setTransactionType(TxnType.P2M.name());
		transaction.setTransactionDate(new Date());
		WalletTransaction tempTransaction = transactionService.createTransaction(transaction);
		return tempTransaction;
	}

	private WalletTransaction doMoneyTransfer(TransactionRequest request, Map<String, Object> p2mContext) {
		Wallet payerWallet = (Wallet) p2mContext.get(ContextConstant.USER_WALLET);

		Double balance = payerWallet.getBalance();
		Double amount = request.getAmount();

		WalletTransaction transaction = new WalletTransaction();

		balance -= amount;
		payerWallet.setBalance(balance);

		transaction.setPayerRemainingAmount(balance);

		MerchantWallet payeeMerchantWallet = (MerchantWallet) p2mContext.get(ContextConstant.MERCHANT_WALLET);

		balance = payeeMerchantWallet.getBalance();
		balance += amount;
		transaction.setPayeeRemainingAmount(balance);
		payeeMerchantWallet.setBalance(balance);

		walletService.update(payerWallet);
		merchantWalletService.createOrUpdate(payeeMerchantWallet);
		return transaction;
	}
	
	// API FOR SENDING MONEY TO A PERSION
	@PostMapping("/Transaction/P2P")
	public ResponseEntity<TransactionResponse> payMoneyToPersion(@RequestBody TransactionRequest ptopTransaction) 
	{
		logger.info("p2p transaction received {}",ptopTransaction);
		User payerUser = userService.findByMobileNo(ptopTransaction.getPayerMobileNo());
		User payeeUser = userService.findByMobileNo(ptopTransaction.getPayeeMobileNo());
		
		TransactionResponse transactionResponse = new TransactionResponse();

		if (payerUser == null || payeeUser == null) {
			transactionResponse.setMessage("Transaction couldn't be done : user is not exist");
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.NOT_FOUND);
		}

		transactionResponse.setPayeeName(payeeUser.getFname());
		transactionResponse.setPayerName(payerUser.getFname());

		if (payerUser.getStatus() == "unactive" || payeeUser.getStatus() == "unactive") // user is not active
		{
			transactionResponse.setMessage("Transaction couldn't be done : user is not active");
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.NOT_FOUND);
		}

		Wallet payerWallet = walletService.get(ptopTransaction.getPayerMobileNo());           // wallet is active or not
		Wallet payeeWallet = walletService.get(ptopTransaction.getPayeeMobileNo());
		
		if (payerWallet.getStatus() == "disable" || payeeWallet.getStatus() == "disable") {
			transactionResponse.setMessage("Transaction couldn't be done : wallet is not active");
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.NOT_FOUND);
		}

		Double balance = payerWallet.getBalance();
		Double amount = ptopTransaction.getAmount();

		WalletTransaction transaction = new WalletTransaction();

		if ((amount >= 0) && (balance >= amount)) {
			balance -= amount;

			payerWallet.setBalance(balance);

			transaction.setPayerRemainingAmount(balance);
			balance = payeeWallet.getBalance();
			balance += amount;
			transaction.setPayeeRemainingAmount(balance);
			payeeWallet.setBalance(balance);

			walletService.update(payerWallet);
			walletService.update(payeeWallet);
			
			transaction.setStatus("transaction succesful");
		} else {
			transaction.setStatus(" transaction failed : balance is not sufficient to pay");
			transaction.setPayeeRemainingAmount(payeeWallet.getBalance());
			transaction.setPayerRemainingAmount(payerWallet.getBalance());
		}
		transaction.setPayeeMobileNo(ptopTransaction.getPayeeMobileNo());
		transaction.setPayerMobileNo(ptopTransaction.getPayerMobileNo());
		transaction.setAmount(ptopTransaction.getAmount());
		transaction.setTransactionType(TxnType.P2P.name());
		transaction.setTransactionDate(new Date());

		WalletTransaction currentTransaction = transactionService.createTransaction(transaction);
        
		transactionResponse.setMessage("Transaction Successful");
		transactionResponse.setDate(currentTransaction.getTransactionDate());
		transactionResponse.setId(currentTransaction.getId());
		transactionResponse.setOrderId(ptopTransaction.getOrderId());
		return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	}

	// API FOR ADDINGMONEY
	@PostMapping("Transaction/addMoney")
	public ResponseEntity<TransactionResponse> addMoney(@RequestBody TransactionRequest addMoneyTransaction) {

		User payerUser = userService.findByMobileNo(addMoneyTransaction.getPayerMobileNo());

		TransactionResponse transactionResponse = new TransactionResponse();
		if (payerUser == null) {
			transactionResponse.setMessage("Transaction couldn't be done : user is not exist");
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.NOT_FOUND);
		}

		transactionResponse.setPayerName(payerUser.getFname());
		transactionResponse.setPayeeName(payerUser.getFname());
		if (payerUser.getStatus() == "unactive") // user is not active
		{
			transactionResponse.setMessage("Transaction couldn't be done : user is not active");
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.NOT_FOUND);
		}

		Wallet payeeWallet = walletService.get(addMoneyTransaction.getPayeeMobileNo());
		Bank payerBank = bankService.get(addMoneyTransaction.getPayeeMobileNo());
		if (payeeWallet.getStatus() == "disable") {
			transactionResponse.setMessage("Transaction couldn't be done : wallet is not active");
			transactionResponse.setDate(new Date());
			return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.NOT_FOUND);
		}

		Double balance = payeeWallet.getBalance();
		Double amount = addMoneyTransaction.getAmount();

		WalletTransaction transaction = new WalletTransaction();

		if ((amount > 0) && (balance >= amount)) {
			balance -= amount;
			payerBank.setBalance(balance);
			transaction.setPayerRemainingAmount(balance);

			balance = payeeWallet.getBalance();
			balance += amount;
			transaction.setPayeeRemainingAmount(balance);
			payeeWallet.setBalance(balance);

			bankService.saveBankDetail(payerBank);
			walletService.update(payeeWallet);
			transaction.setStatus("transaction succesful");
		} else {
			transaction.setStatus(" transaction failed : balance is not sufficient to pay");
			transaction.setPayeeRemainingAmount(payeeWallet.getBalance());
			transaction.setPayerRemainingAmount(payerBank.getBalance());
		}

		transaction.setPayeeMobileNo(addMoneyTransaction.getPayeeMobileNo());
		transaction.setPayerMobileNo(addMoneyTransaction.getPayerMobileNo());
		transaction.setAmount(addMoneyTransaction.getAmount());
		transaction.setTransactionType(TxnType.P2P.name());
		transaction.setTransactionDate(new Date());
		transaction.setOrderId(addMoneyTransaction.getOrderId());
		WalletTransaction tempTransaction = transactionService.createTransaction(transaction);

		transactionResponse.setMessage(" Transaction successful, Repeese have been added ");
		transactionResponse.setDate(tempTransaction.getTransactionDate());
		transactionResponse.setOrderId(addMoneyTransaction.getOrderId());
		transactionResponse.setId(tempTransaction.getId());
		return new ResponseEntity<TransactionResponse>(transactionResponse, HttpStatus.OK);
	}

}
