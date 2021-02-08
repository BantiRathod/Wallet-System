package com.banti.wallet.ums.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.banti.wallet.ums.elasticsearch.models.ElasticPersonWallet;

public interface ElasticPersonWalletRepository extends ElasticsearchRepository<ElasticPersonWallet,String>{

}
