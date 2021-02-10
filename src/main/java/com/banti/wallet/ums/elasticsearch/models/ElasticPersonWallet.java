package com.banti.wallet.ums.elasticsearch.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="elastic_person_wallet",shards=1)
public class ElasticPersonWallet {

    @Id
	private String mobileNo;
    @Field(type = FieldType.Double, name = "balance")
	private Double balance;
    @Field(type = FieldType.Text, name = "status")
	private String status;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", name="createdDate")
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
