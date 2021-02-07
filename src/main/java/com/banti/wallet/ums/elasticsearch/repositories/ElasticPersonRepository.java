package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;

public interface ElasticPersonRepository extends ElasticsearchRepository<ElasticPerson,Long>{

	ElasticPerson findByMobileNo(String mobileNo);

}
