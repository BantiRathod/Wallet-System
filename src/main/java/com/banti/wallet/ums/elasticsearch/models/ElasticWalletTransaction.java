package com.banti.wallet.ums.elasticsearch.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="ElasticElasticWalletTransaction",shards=1)
public class ElasticWalletTransaction {
	
	@Id
	private Long id;
	@Field(type=FieldType.Text, name="payerMobileNo")
	private String payerMobileNo;
	@Field(type=FieldType.Text, name="payeeMobileNo")
	private String payeeMobileNo;
	@Field(type=FieldType.Double, name="amount")
	private Double amount;
	@Field(type=FieldType.Double, name="payerRemainingAmount")
	private Double payerRemainingAmount;
	@Field(type=FieldType.Double, name="payeeRemainingAmounto")
	private Double payeeRemainingAmount;
	@Field(type=FieldType.Text, name="status")
	private String status;
	@Field(type=FieldType.Text, name="transactionType")
	private String transactionType;
	 @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", name="transactionDate")
	private Date transactionDate;
	@Field(type=FieldType.Text, name="orderId")
	private String orderId;
	
	public ElasticWalletTransaction() {}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPayerMobileNo() {
		return payerMobileNo;
	}
	public void setPayerMobileNo(String payerMobileNo) {
		this.payerMobileNo = payerMobileNo;
	}
	public String getPayeeMobileNo() {
		return payeeMobileNo;
	}
	public void setPayeeMobileNo(String payeeMobileNo) {
		this.payeeMobileNo = payeeMobileNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getPayerRemainingAmount() {
		return payerRemainingAmount;
	}
	public void setPayerRemainingAmount(Double payerRemainingAmount) {
		this.payerRemainingAmount = payerRemainingAmount;
	}
	public Double getPayeeRemainingAmount() {
		return payeeRemainingAmount;
	}
	public void setPayeeRemainingAmount(Double payeeRemainingAmount) {
		this.payeeRemainingAmount = payeeRemainingAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "WalletTransaction [id=" + id + ", payerMobileNo=" + payerMobileNo + ", payeeMobileNo=" + payeeMobileNo
				+ ", amount=" + amount + ", payerRemainingAmount=" + payerRemainingAmount + ", payeeRemainingAmount="
				+ payeeRemainingAmount + ", status=" + status + ", transactionType=" + transactionType
				+ ", transactionDate=" + transactionDate + ", orderId=" + orderId + "]";
	}
	
	
}
