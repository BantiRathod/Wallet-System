package com.banti.wallet.ums.validator.request;

import org.springframework.stereotype.Service;

import com.banti.wallet.ums.requestEntities.PaginationRequest;

@Service 
public class PaginationRequestValidator {
   
	public void  paginationRequestValidation(PaginationRequest paginationRequest) throws Exception
	{
		if(paginationRequest.getPageNo()<0)
			throw new Exception(" pageNo is not valid, pageNo is = " + paginationRequest.getPageNo());
		
		if(paginationRequest.getPageSize()<=0)
			throw new Exception(" pageSize is not valid, pageNo is = " + paginationRequest.getPageNo());
		
		if(paginationRequest.getUserId()<0)
			throw new Exception(" invalid user Id, " + paginationRequest.getPageNo());
		
	
    }
}