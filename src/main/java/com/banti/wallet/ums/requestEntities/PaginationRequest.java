package com.banti.wallet.ums.requestEntities;

public class PaginationRequest {
       private Long userId;
       private int pageNo;
       private int pageSize;
       
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public String toString() {
		return "PaginationRequest [userId=" + userId + ", pageNo=" + pageNo + ", pageSize=" + pageSize + "]";
	}
              
}
