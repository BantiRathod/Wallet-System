package com.banti.wallet.ums.requestEntities;

public class UpdateMerchantWalletRequest {

	    private String mobileNo;
	    //private Double balance;
	    
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
		/*
		 * public Double getBalance() { return balance; } public void setBalance(Double
		 * balance) { this.balance = balance; }
		 */
		
		@Override
		public String toString() {
			return "UpdateMerchantWalletRequest [mobileNo=" + mobileNo + "]";
		}
	    
	    
	
}
