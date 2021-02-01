package com.banti.wallet.ums.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bank 
{
	    @Id
		private String accountNo;
		private Double balance;
		private Date date;
		private String bankName;
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public Double getBalance() {
			return balance;
		}
		public void setBalance(Double balance) {
			this.balance = balance;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		@Override
		public String toString() {
			return "Bank [accountNo=" + accountNo + ", balance=" + balance + ", date=" + date + ", bankName=" + bankName
					+ "]";
		}
		
				
}
