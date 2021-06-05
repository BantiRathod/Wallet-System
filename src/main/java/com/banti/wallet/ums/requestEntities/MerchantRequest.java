package com.banti.wallet.ums.requestEntities;



public class MerchantRequest {

	private String shopName;
	private String address;
	private String email;
	private String mobileNo;
	
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

	
	@Override
	public String toString() {
		return "MerchantRequest [shopName=" + shopName + ", address=" + address + ", email=" + email + ", mobileNo="
				+ mobileNo + "]";
	}
}
