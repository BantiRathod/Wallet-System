package com.banti.wallet.ums.controller;

public class TransactionRequest {
	private String orderId;
	private String PayerMobileNo;
	private String PayeeMobileNo;
	private Double amount;


	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayerMobileNo() {
		return PayerMobileNo;
	}

	public void setPayerMobileNo(String payerMobileNo) {
		PayerMobileNo = payerMobileNo;
	}

	public String getPayeeMobileNo() {
		return PayeeMobileNo;
	}

	public void setPayeeMobileNo(String payeeMobileNo) {
		PayeeMobileNo = payeeMobileNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "TransactionRequest [orderId=" + orderId + ", PayerMobileNo=" + PayerMobileNo + ", PayeeMobileNo="
				+ PayeeMobileNo + ", amount=" + amount + "]";
	}
	
	
}
