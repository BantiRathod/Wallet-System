package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.banti.wallet.ums.elasticsearch.models.ElasticMerchant;

public interface ElasticMerchantRepository extends ElasticsearchRepository<ElasticMerchant , Long> {

	ElasticMerchant findByMobileNo(String payeeMobileNo);

}
