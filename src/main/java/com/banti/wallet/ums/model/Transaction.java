package com.banti.wallet.ums.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String payerNo;
	private String payeeNo;
	private Double amount;
	private String status;
	
	public Transaction() {}
	public Transaction(Long id, String payerNo, String payeeNo, Double amount, String status) {
		this.id = id;
		this.payerNo = payerNo;
		this.payeeNo = payeeNo;
		this.amount = amount;
		this.status=status;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPayerNo() {
		return payerNo;
	}
	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}
	public String getPayeeNo() {
		return payeeNo;
	}
	public void setPayeeNo(String payeeNo) {
		this.payeeNo = payeeNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", payerNo=" + payerNo + ", payeeNo=" + payeeNo + ", amount=" + amount
				+ ", status=" + status + "]";
	}
	
	
	
}
