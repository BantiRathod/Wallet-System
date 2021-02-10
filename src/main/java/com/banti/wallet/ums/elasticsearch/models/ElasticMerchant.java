package com.banti.wallet.ums.elasticsearch.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="elastic_merchant",shards=1)
public class ElasticMerchant {
	
	@Id
	private Long   merchantId;
	@Field(type=FieldType.Text, name="shopName")
	private String shopName;
	@Field(type=FieldType.Text, name="address")
	private String address;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", name="registerDate")
	private Date registerDate;
	@Field(type=FieldType.Text, name="email")
	private String email;
	@Field(type=FieldType.Text, name="mobileNo")
	private String mobileNo;
	@Field(type=FieldType.Text, name="status")
	private String status;
	@Field(type=FieldType.Text, name="password")
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
