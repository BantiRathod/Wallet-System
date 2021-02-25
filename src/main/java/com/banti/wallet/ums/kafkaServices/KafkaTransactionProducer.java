package com.banti.wallet.ums.kafkaServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.model.WalletTransaction;
import com.google.gson.Gson;


@Service
public class KafkaTransactionProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkatemp;
	@Autowired
	private Gson gson;
	
	public void transactionProducer(WalletTransaction transaction) throws Exception
	{
		final String topic = "WalletTransaction";
	    kafkatemp.send(topic, gson.toJson(transaction));            // to convert user object into Json formatted string.
		
	}

}
