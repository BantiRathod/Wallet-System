package com.banti.wallet.ums.model;

import java.util.Date;
import javax.persistence.Entity;

import javax.persistence.Id;

@Entity
public class MerchantWallet 
{
	@Id
    private String mobileNo;
    private Double balance;
    private Date merchantWalletcreatedDate;
    private String status;
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
	public Date getMerchantWalletcreatedDate() {
		return merchantWalletcreatedDate;
	}
	public void setMerchantWalletcreatedDate(Date merchantWalletcreatedDate) {
		this.merchantWalletcreatedDate = merchantWalletcreatedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "MerchantWallet [mobileNo=" + mobileNo + ", balance=" + balance + ", merchantWalletcreatedDate="
				+ merchantWalletcreatedDate + ", status=" + status + "]";
	}
    
         
}
