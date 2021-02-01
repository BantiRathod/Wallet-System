package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banti.wallet.ums.model.MerchantWallet;

public interface MerchantWalletRepository extends JpaRepository<MerchantWallet,String> {

}
