package com.banti.wallet.ums.service;

import java.util.Date;
import java.util.Map;

//import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.enums.TxnType;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.pagination.PaginationRequest;
import com.banti.wallet.ums.repository.TransactionRepository;
import com.banti.wallet.ums.transactionClassesToPayment.TransactionRequest;
import com.banti.wallet.ums.validator.business.TransactionBusinessValidator;

@Service
@Transactional
public class TransactionService
{
	Logger logger=LoggerFactory.getLogger(TransactionService.class);
	@Autowired
	private PersonService userService;
	
	@Autowired
	private TransactionRepository trepo;

	@Autowired
	private TransactionBusinessValidator transactionBusinessValidator;

	@Autowired
	@Qualifier(value ="userWalletService")
	private MoneyMovementService userMoneyMovementService;
    
	@Autowired
	@Qualifier(value="merchantWalletService")
	private  MoneyMovementService merchantMoneyMovementService;
	
	public Page<WalletTransaction> listAll(PaginationRequest paginationRequest) throws Exception
	{
		Person user=userService.get(paginationRequest.getUserId());
		if(user==null) {
			throw new Exception("user does not exist");
		}
	    Pageable pageable = PageRequest.of(paginationRequest.getPageNo(), paginationRequest.getPageSize(), Sort.Direction.ASC, "id");
	    return trepo.findAllByPayerNo(user.getMobileNo(),pageable);
	}
	
	public WalletTransaction getTransaction(Long id)
	{
		return trepo.findById(id).get();
	}
	
	public WalletTransaction saveTransaction(WalletTransaction transaction)
	{
		return trepo.save(transaction);
	}

	public WalletTransaction performP2M(TransactionRequest request, Map<String, Object> p2mContext) throws Exception {
		logger.info("current context {}",p2mContext);
		transactionBusinessValidator.p2mValidation(request, p2mContext);
		logger.info("context after business validation {}",p2mContext);
		WalletTransaction transaction = doMoneyTransfer(request, p2mContext);
		//logger.info("context after money movement {}",p2mContext);
		WalletTransaction walletTransaction = createTransaction(request, transaction);
		logger.info("generated transaction {}",walletTransaction);
		return walletTransaction;	
	}
	
	private WalletTransaction createTransaction(TransactionRequest request, WalletTransaction transaction) {
		transaction.setStatus("transaction succesful");
		transaction.setPayeeMobileNo(request.getPayeeMobileNo());
		transaction.setPayerMobileNo(request.getPayerMobileNo());
		transaction.setAmount(request.getAmount());
		transaction.setOrderId(request.getOrderId());
		transaction.setTransactionType(TxnType.P2M.name());
		transaction.setTransactionDate(new Date());
		WalletTransaction tempTransaction = saveTransaction(transaction);
		return tempTransaction;
	}

	private WalletTransaction doMoneyTransfer(TransactionRequest request, Map<String, Object> p2mContext) {
		PersonWallet payerUserWallet = (PersonWallet) p2mContext.get(ContextConstant.USER_WALLET);
		
		WalletTransaction transaction = new WalletTransaction();
		PersonWallet updatedPayerUserWallet = (PersonWallet) userMoneyMovementService.debitMoney(payerUserWallet, request.getAmount());
		transaction.setPayerRemainingAmount(updatedPayerUserWallet.getBalance());

		MerchantWallet payeeMerchantWallet = (MerchantWallet) p2mContext.get(ContextConstant.MERCHANT_WALLET);

		MerchantWallet updateMerchantWallet = (MerchantWallet) merchantMoneyMovementService.creditMoney(payeeMerchantWallet, request.getAmount());
		transaction.setPayeeRemainingAmount(updateMerchantWallet.getBalance());
		return transaction;
	}
}
