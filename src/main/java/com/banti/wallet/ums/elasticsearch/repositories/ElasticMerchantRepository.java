package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;

@Repository
public interface ElasticMerchantRepository extends ElasticsearchRepository<ElasticMerchant , Long> {

	ElasticMerchant findByMobileNo(String payeeMobileNo);

}
