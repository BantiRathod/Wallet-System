package com.banti.wallet.ums.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Wallet {
    @Id
	private String mobileNo;
	private Double balance;
	
	public Wallet(){}
	
	public Wallet(String mobileNo, Double balance) {
		this.mobileNo = mobileNo;
		this.balance = balance;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Wallet [mobileNo=" + mobileNo + ", balance=" + balance + "]";
	}
	
}
