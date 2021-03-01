package com.banti.wallet.ums.model;

import java.util.Date;
import javax.persistence.Entity;

import javax.persistence.Id;

@Entity
public class MerchantWallet extends BaseWallet
{
	@Id
    private String mobileNo;
    private Double balance;
    private Date merchantWalletcreatedDate;
    private String status;
    
    public MerchantWallet() {} 
    
	public MerchantWallet(String mobileNo, Double balance, String status, Date merchantWalletcreatedDate) {
		this.mobileNo = mobileNo;
		this.balance = balance;
		this.merchantWalletcreatedDate = merchantWalletcreatedDate;
		this.status = status;
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
