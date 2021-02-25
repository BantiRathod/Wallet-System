package com.banti.wallet.ums.kafkaServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.elasticsearch.models.ElasticWalletTransaction;
import com.banti.wallet.ums.model.WalletTransaction;
import com.banti.wallet.ums.service.TransactionService;
import com.google.gson.Gson;

@Service
public class KafkaTransactionConsumer {
	
	Logger logger = LoggerFactory.getLogger(KafkaTransactionConsumer.class);
	@Autowired 
	private	TransactionService transactionService;
	@Autowired
	private Gson gson;
	
         @KafkaListener(topics="WalletTransaction" , groupId="myGroupId_5")
         public void  transactionConsumer(String transaction)                              // message type should be string.
         {
				/*
				 * logger.info("consumed message: "+ transaction);
				 * logger.info("model converted value"+ gson.fromJson(transaction,
				 * WalletTransaction.class).toString());
				 */
        	 
        	 WalletTransaction tempTransaction = gson.fromJson(transaction, WalletTransaction.class);
     		 logger.info("consumed message {}",tempTransaction);
        	 
        	ElasticWalletTransaction elasticWalletTransaction = new ElasticWalletTransaction();
    		
    		elasticWalletTransaction.setAmount(tempTransaction.getAmount());
    		elasticWalletTransaction.setId(tempTransaction.getId());
    		elasticWalletTransaction.setOrderId(tempTransaction.getOrderId());
    		elasticWalletTransaction.setPayeeMobileNo(tempTransaction.getPayeeMobileNo());
    		elasticWalletTransaction.setPayerMobileNo(tempTransaction.getPayerMobileNo());
    		elasticWalletTransaction.setPayeeRemainingAmount(tempTransaction.getPayeeRemainingAmount());
    		elasticWalletTransaction.setPayerRemainingAmount(tempTransaction.getPayerRemainingAmount());
    		elasticWalletTransaction.setStatus(tempTransaction.getStatus());
    		elasticWalletTransaction.setTransactionDate(tempTransaction.getTransactionDate());
    		elasticWalletTransaction.setTransactionType(tempTransaction.getTransactionType());
    		//TO STORE TRANSACTION IN ELASTICSEARCH DATABASE
    		transactionService.saveElasticTransaction(elasticWalletTransaction);
        
         }
}