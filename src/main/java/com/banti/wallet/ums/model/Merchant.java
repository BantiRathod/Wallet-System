package com.banti.wallet.ums.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Merchant 
{	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE)
		private Long   merchantId;
		private String shopName;
		private String address;
		private Date  registerDate;
		private String email;
		private String mobileNo;
		private String status;
		private String password;
		
		public Long getMerchantId() {
			return merchantId;
		}
		public void setMerchantId(Long merchantId) {
			this.merchantId = merchantId;
		}
		public String getShopName() {
			return shopName;
		}
		public void setShopName(String shopName) {
			this.shopName = shopName;
		}
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Date getRegisterDate() {
			return registerDate;
		}
		public void setRegisterDate(Date registerDate) {
			this.registerDate = registerDate;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@Override
		public String toString() {
			return "Merchant [merchantId=" + merchantId + ", shopName=" + shopName + ", address=" + address
					+ ", registerDate=" + registerDate + ", email=" + email + ", mobileNo=" + mobileNo + ", status="
					+ status + ", password=" + password + "]";
		}
		
	
		
		
}
