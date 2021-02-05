package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banti.wallet.ums.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,String> {


}
