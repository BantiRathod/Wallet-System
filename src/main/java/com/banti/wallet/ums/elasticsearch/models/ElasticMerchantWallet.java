package com.banti.wallet.ums.elasticsearch.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="ElasticMerchantWallet",shards=1)
public class ElasticMerchantWallet {
	
	@Id
    private String mobileNo;
	@Field(type=FieldType.Double, name="balance")
    private Double balance;
	 @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", name="registerDate")
    private Date registerDate;
	@Field(type=FieldType.Text, name="status")
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
		return registerDate;
	}
	public void setMerchantWalletcreatedDate(Date registerDate) {
		this.registerDate = registerDate;
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
				+ registerDate + ", status=" + status + "]";
	}
    
}
