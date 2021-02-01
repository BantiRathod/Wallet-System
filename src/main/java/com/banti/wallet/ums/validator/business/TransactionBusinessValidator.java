package com.banti.wallet.ums.validator.business;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.constant.ContextConstant;
import com.banti.wallet.ums.controller.TransactionRequest;
import com.banti.wallet.ums.controller.TransactionResponse;
import com.banti.wallet.ums.enums.AccountStatus;
import com.banti.wallet.ums.model.Merchant;
import com.banti.wallet.ums.model.MerchantWallet;
import com.banti.wallet.ums.model.User;
import com.banti.wallet.ums.model.Wallet;
import com.banti.wallet.ums.repository.MerchantRepository;
import com.banti.wallet.ums.repository.MerchantWalletRepository;
import com.banti.wallet.ums.repository.UserRepository;
import com.banti.wallet.ums.service.MerchantWalletService;
import com.banti.wallet.ums.service.WalletService;


@Service
public class TransactionBusinessValidator {
	@Autowired
	private WalletService walletService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MerchantRepository merchantRepository;
	@Autowired
	private MerchantWalletService merchantWalletService;
	
	
	
	
	public void p2mValidation(TransactionRequest request, Map<String, Object> p2mContext) throws Exception {
		// check user exist
		User user = userRepository.findByMobileNo(request.getPayerMobileNo());
		if(user==null) {
			throw new Exception("user account not exist in system with mobile number "+request.getPayerMobileNo());	
		}else if(!user.getStatus().equalsIgnoreCase("ACTIVE")) {
			throw new Exception("user account is not active");
		}
		p2mContext.put(ContextConstant.USER_ACCOUNT, user);
		
		Merchant merchant = merchantRepository.findByMobileNo(request.getPayeeMobileNo());
		if(merchant==null) {
			throw new Exception("merchant account not exist in system with mobile number "+request.getPayeeMobileNo());
		}
		p2mContext.put(ContextConstant.MERCHANT_ACCOUNT, merchant);
		
		//check user has sufficient amt
		Wallet userWallet = walletService.get(request.getPayeeMobileNo());
		if(userWallet.getBalance()<request.getAmount()) {
			throw new Exception(String.format("user does not have sufficient balance, current balance;%d, request txnAmt: %d",userWallet.getBalance(),request.getAmount()));
		}		
		p2mContext.put(ContextConstant.USER_WALLET, userWallet);
		
		MerchantWallet payeeMerchantWallet = merchantWalletService.get(request.getPayeeMobileNo());
		if (AccountStatus.DISABLED.name().equalsIgnoreCase(payeeMerchantWallet.getStatus())) {
			throw new Exception("merchant is not active");
		}
		p2mContext.put(ContextConstant.MERCHANT_WALLET, payeeMerchantWallet);
	}

}
