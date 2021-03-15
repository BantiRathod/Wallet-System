package com.banti.wallet.ums.scheduling;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "elastic_payload" , shards = 1)
public class ElasticPayout {
	
	@Id
	private Long payloadId;
	@Field(type=FieldType.Text, name="merchantMobileNo")
	private String merchantMobileNo;
	@Field(type=FieldType.Double , name="amount")
	private double amount;
	@Field(type=FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", name="sendMoneyDate")
	private Date sendMoneyDate;
	@Field(type=FieldType.Text, name="status")
	private String status;

	
	public void Payload() {}
	
	public void Payload(Long payloadId, String merchantMobileNo, double amount, Date sendMoneyDate, String status,
			int totalTransaction) {
		this.payloadId = payloadId;
		this.merchantMobileNo = merchantMobileNo;
		this.amount = amount;
		this.sendMoneyDate = sendMoneyDate;
		this.status = status;
	
	}

	public Long getPayloadId() {
		return payloadId;
	}

	public void setPayloadId(Long payloadId) {
		this.payloadId = payloadId;
	}

	public String getMerchantMobileNo() {
		return merchantMobileNo;
	}

	public void setMerchantMobileNo(String merchantMobileNo) {
		this.merchantMobileNo = merchantMobileNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getSendMoneyDate() {
		return sendMoneyDate;
	}

	public void setSendMoneyDate(Date sendMoneyDate) {
		this.sendMoneyDate = sendMoneyDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Payload [payloadId=" + payloadId + ", merchantMobileNo=" + merchantMobileNo + ", amount=" + amount
				+ ", sendMoneyDate=" + sendMoneyDate + ", status=" + status + "]";
	}	
}
