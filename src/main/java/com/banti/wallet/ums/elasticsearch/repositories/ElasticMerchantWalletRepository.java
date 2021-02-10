package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.banti.wallet.ums.elasticsearch.models.ElasticMerchantWallet;

@Repository
public interface ElasticMerchantWalletRepository extends ElasticsearchRepository<ElasticMerchantWallet,String> {

}
