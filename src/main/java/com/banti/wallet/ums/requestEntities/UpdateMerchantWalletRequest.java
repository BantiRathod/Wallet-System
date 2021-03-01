package com.banti.wallet.ums.requestEntities;

public class UpdateMerchantWalletRequest {

	    private String mobileNo;
	    
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
	
		
		@Override
		public String toString() {
			return "UpdateMerchantWalletRequest [mobileNo=" + mobileNo + "]";
		}
	    
	    
	
}
