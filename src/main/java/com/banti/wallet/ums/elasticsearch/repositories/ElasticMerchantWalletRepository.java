package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.banti.wallet.ums.elasticsearch.models.ElasticMerchantWallet;

public interface ElasticMerchantWalletRepository extends ElasticsearchRepository<ElasticMerchantWallet,String> {

}
