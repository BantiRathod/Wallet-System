package com.banti.wallet.ums.requestEntities;

public class TransactionRequest {
	private String orderId;
	private String payerMobileNo;
	private String payeeMobileNo;
	private Double amount;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	
	@Override
	public String toString() {
		return "TransactionRequest [orderId=" + orderId + ", payerMobileNo=" + payerMobileNo + ", payeeMobileNo="
				+ payeeMobileNo + ", amount=" + amount + "]";
	}
	
}
