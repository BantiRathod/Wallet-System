package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.banti.wallet.ums.elasticsearch.models.ElasticWalletTransaction;

public interface ElasticWalletTransactionRepository extends ElasticsearchRepository<ElasticWalletTransaction, Long> {

	public Iterable<ElasticWalletTransaction> findAllByPayerNo(String mobileNo);
		
	
}
