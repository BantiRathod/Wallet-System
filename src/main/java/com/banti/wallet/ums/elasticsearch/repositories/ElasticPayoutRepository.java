package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.banti.wallet.ums.scheduling.ElasticPayout;

public interface ElasticPayoutRepository extends ElasticsearchRepository<ElasticPayout,Long>{
	
}
