package com.banti.wallet.ums.scheduling;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Payout {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long payloadId;
	private String merchantMobileNo;
	private double amount;
	private Date sendMoneyDate;
	private String status;
	
	public Payout() {}
	
	public Payout(Long payloadId, String merchantMobileNo, double amount, Date sendMoneyDate, String status,
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
				+ ", sendMoneyDate=" + sendMoneyDate + ", status=" + status + ", totalTransaction=" + "]";
	}	
}
