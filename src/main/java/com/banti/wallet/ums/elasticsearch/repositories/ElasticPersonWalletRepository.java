package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;

@Repository
public interface ElasticPersonWalletRepository extends ElasticsearchRepository<ElasticPersonWallet,String>{

}
