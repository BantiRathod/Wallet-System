package com.banti.wallet.ums.requestEntities;

public class PersonWalletRequest {

	private Double balance;
	private String mobileNo;
	
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	@Override
	public String toString() {
		return "PersonWalletRequest [balance=" + balance + ", mobileNo=" + mobileNo + "]";
	}
	
	
}
