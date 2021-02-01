package com.banti.wallet.ums.validator.request;

import com.banti.wallet.ums.controller.TransactionRequest;

public class TransactionRequestValidator {
	
	public static void  p2mRequestValidator(TransactionRequest request) throws Exception {
		if(request.getAmount()<=0) {
			throw new Exception("Invaid Request Parameter, amount is negative or zero "+request.getAmount());
		}
		if(request.getPayerMobileNo().equals(request.getPayeeMobileNo())) {
			throw new Exception("Invaid Request Parameter, payer and payee is same");
		}
	}

}
