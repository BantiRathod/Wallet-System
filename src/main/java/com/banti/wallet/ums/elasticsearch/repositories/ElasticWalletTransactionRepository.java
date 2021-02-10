package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.banti.wallet.ums.elasticsearch.models.ElasticWalletTransaction;

@Repository
public interface ElasticWalletTransactionRepository extends ElasticsearchRepository<ElasticWalletTransaction, Long> {

	public Iterable<ElasticWalletTransaction> findAllByPayerMobileNo(String mobileNo);

	public Iterable<ElasticWalletTransaction> findAllByPayeeMobileNo(String mobileNo);
		
	
}
