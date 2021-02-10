package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.banti.wallet.ums.elasticsearch.models.ElasticPerson;

@Repository
public interface ElasticPersonRepository extends ElasticsearchRepository<ElasticPerson,Long>{

	ElasticPerson findByMobileNo(String mobileNo);

	ElasticPerson findUserByUserName(String username);

}
