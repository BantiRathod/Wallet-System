package com.banti.wallet.ums.requestEntities;


public class UpdateMerchantRequest {
	
	private String shopName;
	private String address;
	
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

	
	@Override
	public String toString() {
		return "UpdateMerchantRequest [shopName=" + shopName + ", address=" + address + "+ ]";
	}
	
	
}
