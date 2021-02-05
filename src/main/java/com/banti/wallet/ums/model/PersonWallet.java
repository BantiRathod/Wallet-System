package com.banti.wallet.ums.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonWallet extends BaseWallet{
    @Id
	private String mobileNo;
	private Double balance;
	private String status;
	private Date createdDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "Wallet [mobileNo=" + mobileNo + ", balance=" + balance + ", status=" + status + ", createdDate="
				+ createdDate + "]";
	}
	
	
	
}
