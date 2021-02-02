package com.banti.wallet.ums.transactionClassesToPayment;

import java.util.Date;

public class BadTransaction {
	private String message;
	private Long transactionId;
	private Date date;
	private String orderId; 
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "BadTransaction [message=" + message + ", transactionId=" + transactionId + ", date=" + date +" orderId="+ orderId+ "]";
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

  	
}
