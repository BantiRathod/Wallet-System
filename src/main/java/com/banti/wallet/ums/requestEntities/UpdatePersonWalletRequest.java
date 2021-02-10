package com.banti.wallet.ums.requestEntities;

public class UpdatePersonWalletRequest {

	private String mobileNo;
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Override
	public String toString() {
		return "UpdatePersonWalletRequest [mobileNo=" + mobileNo + "]";
	}
	
	
}
