package com.banti.wallet.ums.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;
import com.banti.wallet.ums.elasticsearch.models.ElasticWalletTransaction;
import com.banti.wallet.ums.elasticsearch.repositories.ElasticPersonWalletRepository;
import com.banti.wallet.ums.elasticsearch.repositories.ElasticWalletTransactionRepository;
import com.banti.wallet.ums.enums.TxnType;
import com.banti.wallet.ums.kafkaServices.KafkaTransactionProducer;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.Person;
import com.banti.wallet.ums.model.PersonWallet;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.repository.PersonWalletRepository;
import com.banti.wallet.ums.repository.TransactionRepository;
import com.banti.wallet.ums.requestEntities.AddMoneyTransactionRequest;
//import com.banti.wallet.ums.requestEntities.PaginationRequest;
import com.banti.wallet.ums.requestEntities.TransactionRequest;
import com.banti.wallet.ums.validator.business.TransactionBusinessValidator;

@Service
public class TransactionService
{
	Logger logger=LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private KafkaTransactionProducer kafkaTransactionProducer;
	
	@Autowired
	private PersonWalletRepository personWalletRepository;
	@Autowired
	private PersonService personService;
	@Autowired
	private  ElasticPersonWalletRepository elasticPersonWalletRepository;
	@Autowired
	private TransactionRepository transactionrepository;
    @Autowired
    private ElasticWalletTransactionRepository elasticWalletTransactionRepository; 
	@Autowired
	private PersonWalletService personWalletService;
	@Autowired
	private TransactionBusinessValidator transactionBusinessValidator;
	@Autowired
	@Qualifier(value ="personWalletService")
	private MoneyMovementService personMoneyMovementService;
    
	@Autowired
	@Qualifier(value="merchantWalletService")
	private  MoneyMovementService merchantMoneyMovementService;
	
	public Iterable<ElasticWalletTransaction> getListOfAllTransaction(Long userId) throws Exception
	{
		//TO CHECK THAT USER IS AVAILABLE OR NOT
		 transactionBusinessValidator.summaryBusinessValidation(userId);
		
		Person person = personService.getPersonMysql(userId);
		List<ElasticWalletTransaction> userTransactionListpayerTime = (List<ElasticWalletTransaction>) elasticWalletTransactionRepository.findAllByPayerMobileNo(person.getMobileNo());
		List<ElasticWalletTransaction> userTransactionListpayeeTime =  (List<ElasticWalletTransaction>) elasticWalletTransactionRepository.findAllByPayeeMobileNo(person.getMobileNo());
		List<ElasticWalletTransaction> list =  new ArrayList<>();
                  list.addAll(userTransactionListpayeeTime);
                  list.addAll(userTransactionListpayerTime);
        return (Iterable<ElasticWalletTransaction>)list;
	
     // BEFORE THE ELASTICSEARCH DATABASE, GOT RESULT IN PAGEABLE FORM
     /*                  
	 * Pageable pageable = PageRequest.of(paginationRequest.getPageNo(),
	 * paginationRequest.getPageSize(), Sort.Direction.ASC, "id"); return
	 * transactionrepository.findAllByPayerNo(person.getMobileNo(),pageable); }
	 */
	}
	
	public String getStatus(Long id) {
		ElasticWalletTransaction elasticWalletTransaction =	elasticWalletTransactionRepository.findById(id).get();
		   return elasticWalletTransaction.getStatus();
	}
	
	public ElasticWalletTransaction getTransaction(Long id)
	{
		return elasticWalletTransactionRepository.findById(id).get();
	}
	
	//TO SAVE IN WALLET_TRANSACTION INDEX
	public void saveElasticTransaction(ElasticWalletTransaction elasticWalletTransaction)
	{
		 elasticWalletTransactionRepository.save(elasticWalletTransaction);
	}
	
	public WalletTransaction saveTransaction(WalletTransaction transaction) throws Exception
	{
		//TRANSACTION HAS BEEN SAVED IN MYSQL DATABASE
		WalletTransaction walletTransaction = transactionrepository.save( transaction);
		logger.info("pused transaction: {}",walletTransaction);
		
		// TO PRODUCE TRANSACTION IN KAFKA BEFORE ELASTIC SEARCH 
		
		kafkaTransactionProducer.transactionProducer(walletTransaction);
		
		// **** MOVED ELASTICTRANSACTION CREATION PART TO KAFKA CONSUMER METHOD***
		
		/*
		 * ElasticWalletTransaction elasticWalletTransaction = new
		 * ElasticWalletTransaction();
		 * 
		 * elasticWalletTransaction.setAmount(consumedTransaction.getAmount());
		 * elasticWalletTransaction.setId(walletTransaction.getId());
		 * elasticWalletTransaction.setOrderId(consumedTransaction.getOrderId());
		 * elasticWalletTransaction.setPayeeMobileNo(consumedTransaction.
		 * getPayeeMobileNo());
		 * elasticWalletTransaction.setPayerMobileNo(consumedTransaction.
		 * getPayerMobileNo());
		 * elasticWalletTransaction.setPayeeRemainingAmount(consumedTransaction.
		 * getPayeeRemainingAmount());
		 * elasticWalletTransaction.setPayerRemainingAmount(consumedTransaction.
		 * getPayerRemainingAmount());
		 * elasticWalletTransaction.setStatus(consumedTransaction.getStatus());
		 * elasticWalletTransaction.setTransactionDate(consumedTransaction.
		 * getTransactionDate());
		 * elasticWalletTransaction.setTransactionType(consumedTransaction.
		 * getTransactionType()); //TO STORE TRANSACTION IN ELASTICSEARCH DATABASE
		 * elasticWalletTransactionRepository.save(elasticWalletTransaction);
		 */
		return walletTransaction;
	}
	

	@Transactional
	public WalletTransaction performP2M(TransactionRequest request, Map<String, Object> p2mContext) throws Exception {
		
		logger.info("current context {}", p2mContext);
		
		transactionBusinessValidator.p2mValidation(request, p2mContext);
		
		logger.info("context after business validation {}",p2mContext);
		
		WalletTransaction transaction = doMoneyTransfer(request, p2mContext);
		
		WalletTransaction walletTransaction = createTransaction(request, transaction);
		
		logger.info("generated transaction {}",walletTransaction);
		return walletTransaction;	
	}
	
	private WalletTransaction createTransaction(TransactionRequest request, WalletTransaction transaction) throws Exception {
		
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

  private WalletTransaction doMoneyTransfer(TransactionRequest request, Map<String, Object> p2mContext) throws RuntimeException {
		PersonWallet payerPersonWallet = (PersonWallet) p2mContext.get(ContextConstant.PAYER_PERSON_WALLET);
		
		WalletTransaction transaction = new WalletTransaction();
		
		PersonWallet updatedPayerPersonWallet = (PersonWallet) personMoneyMovementService.debitMoney(payerPersonWallet, request.getAmount());
		transaction.setPayerRemainingAmount(updatedPayerPersonWallet.getBalance());
        
		MerchantWallet payeeMerchantWallet = (MerchantWallet) p2mContext.get(ContextConstant.MERCHANT_WALLET);
		/*//   ****TRANSACTIONAL PART*****
		 * Double i = 20d; if(request.getAmount().equals(i)) { throw new
		 * RuntimeException("transaction amt is 20 so exception"); }
		 */
		MerchantWallet updateMerchantWallet = (MerchantWallet) merchantMoneyMovementService.creditMoney(payeeMerchantWallet, request.getAmount());
		transaction.setPayeeRemainingAmount(updateMerchantWallet.getBalance());
		
		return transaction;
	}

  
  // TO PERFORM P2P TRANSACTION
	public WalletTransaction performp2p(TransactionRequest request, Map<String,Object> p2pContext ) throws Exception
	{
		logger.info("TransactionRequest received {}",request);
		
		transactionBusinessValidator.p2pValidation(request , p2pContext);
		logger.info("p2pContext received {}",p2pContext);
		
		WalletTransaction transaction = doMoneyTransferToPerson(request,p2pContext);
		
		WalletTransaction tempTransaction = createTransactionP2P(request,transaction);
		logger.info("created transaction {}",tempTransaction);
		return tempTransaction;
			
	}
	
	
   // ## TO DO MONEY TRANSFER  FROM PERSON TO PERSON
	public WalletTransaction doMoneyTransferToPerson(TransactionRequest request,Map<String, Object> p2pContext)
	{
		 
		PersonWallet payerPersonWallet = (PersonWallet) p2pContext.get(ContextConstant.PAYER_PERSON_WALLET); 
		PersonWallet payeePersonWallet = (PersonWallet) p2pContext.get(ContextConstant.PAYEE_PERSON_WALLET);
		
		WalletTransaction transaction = new WalletTransaction();
		
		PersonWallet updatedPayerPersonWallet = (PersonWallet) personMoneyMovementService.debitMoney( payerPersonWallet, request.getAmount());
		transaction.setPayerRemainingAmount(updatedPayerPersonWallet.getBalance());
		PersonWallet updatedPayeePersonWallet = (PersonWallet) personMoneyMovementService.creditMoney( payeePersonWallet, request.getAmount());
		transaction.setPayeeRemainingAmount(updatedPayeePersonWallet.getBalance());
		
		return transaction;
	}
	
	public WalletTransaction createTransactionP2P(TransactionRequest request, WalletTransaction transaction) throws Exception
	{
		transaction.setStatus("transaction succesful");
		transaction.setPayeeMobileNo(request.getPayeeMobileNo());
		transaction.setPayerMobileNo(request.getPayerMobileNo());
		transaction.setAmount(request.getAmount());
		transaction.setOrderId(request.getOrderId());
		transaction.setTransactionType(TxnType.P2P.name());
		transaction.setTransactionDate(new Date());
		WalletTransaction tempTransaction = saveTransaction(transaction);
		return tempTransaction;
	}
	
	//TO PERFOEM ADD MONERY OPERATION
	public WalletTransaction performAddMoney(AddMoneyTransactionRequest request) throws Exception
	{
		     transactionBusinessValidator.addMoneyBusinessValidatoion(request);
		     
		      WalletTransaction  walletTransaction = addMoneyInWallet(request); 
		      
		      WalletTransaction tempWalletTransaction = createTransactionForAddMoney(walletTransaction,request); 
		
		return  tempWalletTransaction;
	}
	
	public WalletTransaction createTransactionForAddMoney(WalletTransaction transaction ,AddMoneyTransactionRequest request) throws Exception 
	{
		transaction.setStatus("transaction succesful");
		transaction.setPayeeMobileNo(request.getMobileNo());
		transaction.setPayerMobileNo(request.getMobileNo());
		transaction.setAmount(request.getAmount());
		transaction.setOrderId(null);
		transaction.setTransactionType(TxnType.ADD_MONEY.name());
		transaction.setTransactionDate(new Date());
		
		WalletTransaction tempTransaction = saveTransaction(transaction);
		
		return tempTransaction;
	}
	public WalletTransaction addMoneyInWallet(AddMoneyTransactionRequest request)
	{
		
		PersonWallet personWallet = personWalletService.getPersonWalletFromMysql(request.getMobileNo());
		logger.info("wallet money before transaction {}",personWallet.getBalance());
		
		WalletTransaction walletTransaction = new WalletTransaction();
		walletTransaction.setPayeeRemainingAmount(personWallet.getBalance()+request.getAmount());
		
		PersonWallet newPersonWallet = new PersonWallet(request.getMobileNo(), personWallet.getBalance()+request.getAmount(),
				personWallet.getStatus(), personWallet.getCreatedDate());
		
		personWalletRepository.save(newPersonWallet);
		logger.info("wallet money after transaction {}",newPersonWallet.getBalance());
		
		ElasticPersonWallet newElasaticpersonWallet = new ElasticPersonWallet(request.getMobileNo(),newPersonWallet.getBalance(),
				newPersonWallet.getStatus(),newPersonWallet.getCreatedDate());
		
		//TO STORE IN ELASTIC SEARCH
		elasticPersonWalletRepository.save( newElasaticpersonWallet);
		
		
		return walletTransaction;
		
	}
	
}
