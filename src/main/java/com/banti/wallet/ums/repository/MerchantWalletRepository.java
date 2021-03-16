package com.banti.wallet.ums.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.banti.wallet.ums.model.MerchantWallet;


public interface MerchantWalletRepository extends JpaRepository<MerchantWallet,String> {

	@Query("select mw from MerchantWallet mw where mw.status= 'ENABLED' ")
	List<MerchantWallet> getEnabledMerchantWalletList();

}
