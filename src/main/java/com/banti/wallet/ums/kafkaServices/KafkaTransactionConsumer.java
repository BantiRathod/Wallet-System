package com.banti.wallet.ums.kafkaServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.banti.wallet.ums.model.WalletTransaction;
import com.google.gson.Gson;

@Service
public class KafkaTransactionConsumer {
	
	Logger logger = LoggerFactory.getLogger(KafkaTransactionConsumer.class);
	
	@Autowired
	private Gson gson;
	
         @KafkaListener(topics="WalletTransaction" , groupId="myGroupId2")
         public  WalletTransaction  transactionConsumer(String transaction)                              // message type should be string.
         {
				/*
				 * logger.info("consumed message: "+ transaction);
				 * logger.info("model converted value"+ gson.fromJson(transaction,
				 * WalletTransaction.class).toString());
				 */
        	 WalletTransaction tempTransaction= gson.fromJson(transaction, WalletTransaction.class);
        	 return tempTransaction;
        	 
         }
}