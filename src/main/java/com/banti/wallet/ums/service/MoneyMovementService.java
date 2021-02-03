package com.banti.wallet.ums.service;

import com.banti.wallet.ums.model.BaseWallet;

public interface MoneyMovementService {

	BaseWallet debitMoney(BaseWallet wallet, double amount);
	BaseWallet creditMoney(BaseWallet wallet, double amount);
}
