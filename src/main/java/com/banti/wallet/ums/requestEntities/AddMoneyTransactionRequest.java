package com.banti.wallet.ums.requestEntities;

public class AddMoneyTransactionRequest {
	private Double amount;
	private String mobileNo;
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	@Override
	public String toString() {
		return "AddMoneyTransactionRequest [amount=" + amount + ", mobileNo=" + mobileNo + "]";
	}
	
	

}
