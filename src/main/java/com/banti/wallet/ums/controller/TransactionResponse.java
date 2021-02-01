package com.banti.wallet.ums.controller;

import java.util.Date;

public class TransactionResponse
{
	private Long id;
	private String payerName;
	private String payeeName;
	private String message;
    private String transactionType;
    private Date date;
    private String orderId;
    
    public TransactionResponse(){};
    
	public TransactionResponse(Long id, String payerName,String orderId, String payeeName, String message, String transactionType,
			Date date) {
		this.id = id;
		this.payerName = payerName;
		this.payeeName = payeeName;
		this.message = message;
		this. orderId= orderId;
		this.transactionType = transactionType;
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
    
    
}
