package com.banti.wallet.ums.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WalletTransaction {
 
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	private String payerMobileNo;
	private String payeeMobileNo;
	private Double amount;
	private Double payerRemainingAmount;
	private Double payeeRemainingAmount;
	private String status;
	private String transactionType;
	private Date transactionDate;
	private String orderId;
	public WalletTransaction() {}
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
